package com.middleeastcontainer.ui.inventory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.middleeastcontainer.core.common.Constants
import com.middleeastcontainer.data.camera.CaptureFileProvider
import com.middleeastcontainer.data.camera.WatermarkUtil
import com.middleeastcontainer.data.storage.ImageFileStore
import com.middleeastcontainer.data.sync.UploadScheduler
import com.middleeastcontainer.domain.model.Sighting
import com.middleeastcontainer.domain.model.UnreadUnit
import com.middleeastcontainer.domain.ocr.ContainerOcrEngine
import com.middleeastcontainer.domain.ocr.DetectedNumber
import com.middleeastcontainer.domain.ocr.UnreadRegion
import com.middleeastcontainer.domain.repository.InventoryRepository
import com.middleeastcontainer.domain.usecase.ValidateContainerNumberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

/** A frame awaiting a decision — nothing was read, or the inspector asked to look. */
data class PendingShot(
    val photoAbsolutePath: String,
    val photoRelativePath: String,
    val detected: List<DetectedNumber>,
    val unread: List<UnreadRegion> = emptyList(),
)

/**
 * A frame that counted itself.
 *
 * Kept visible after the fact rather than confirmed beforehand: the ISO 6346
 * check digit rejects about 96% of single-character misreads, so stopping the
 * inspector on every frame buys little and costs a tap forty times a sweep. What
 * it must not do is hide what happened, hence the undo.
 */
data class AcceptedShot(
    val photoAbsolutePath: String,
    val added: List<String>,
    val duplicates: List<String>,
    val detected: List<DetectedNumber>,
    /** Tags issued for units the camera saw but could not read. */
    val needsAttention: List<String> = emptyList(),
)

data class SweepUiState(
    /** Frames captured but not yet read. The shutter does not wait for these. */
    val processing: Int = 0,
    val pending: PendingShot? = null,
    val message: String? = null,
    val lastShot: AcceptedShot? = null,
)

/**
 * Drives one yard sweep.
 *
 * Every frame is passed through multi-number OCR, and each candidate is validated
 * by its ISO 6346 check digit — which is what makes a photo of a stack usable, as
 * tare weights, max-gross figures and CSC plates cannot pass it.
 *
 * Text shaped like a container number that fails validation is not discarded but
 * recorded: that is the difference between reporting six containers and reporting
 * ten of which four could not be read.
 */
@HiltViewModel
class SweepViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: InventoryRepository,
    private val ocr: ContainerOcrEngine,
    private val validate: ValidateContainerNumberUseCase,
    private val watermark: WatermarkUtil,
    private val captureFiles: CaptureFileProvider,
    private val fileStore: ImageFileStore,
    private val scheduler: UploadScheduler,
) : ViewModel() {

    val sweepId: Long = savedStateHandle.get<String>("sweepId")?.toLongOrNull() ?: 0L

    private val _state = MutableStateFlow(SweepUiState())
    val state = _state.asStateFlow()

    /** Live count, visible while walking so a bad sweep shows early. */
    val sightings = repository.observeSightings(sweepId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Units the camera saw but could not read; the sweep is not done until empty. */
    val unread = repository.observeUnread(sweepId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Named at the start; stamped into every photo and the export. */
    private val _zone = MutableStateFlow("")
    val zone = _zone.asStateFlow()

    private val frameQueue = Channel<File>(Channel.UNLIMITED)

    init {
        // Frames from a session the system killed mid-read are dead weight; at
        // full camera resolution each is several megabytes.
        captureFiles.pruneStaleCaptures()

        viewModelScope.launch {
            _zone.value = repository.sweep(sweepId)?.zone.orEmpty()
        }
        // One consumer, so frames are read in order and memory stays flat.
        viewModelScope.launch {
            for (frame in frameQueue) processFrame(frame)
        }
    }

    fun newCaptureFile(): File = captureFiles.newCaptureFile()

    override fun onCleared() {
        // Without this the consumer coroutine and its channel outlive the screen.
        frameQueue.close()
        super.onCleared()
    }

    fun onCameraError(message: String) {
        _state.update { it.copy(message = message) }
    }

    /**
     * Reads the frame, then files the photo.
     *
     * OCR runs on the raw capture, before watermarking resizes it to the storage
     * edge and burns text into the image — handing OCR the smaller version would
     * cost exactly the detail a distant number in a stack depends on.
     */
    /**
     * Hands the frame to the reader and returns at once.
     *
     * OCR and watermarking together cost one and a half to two and a half
     * seconds. Holding the shutter for that meant a forty-frame sweep spent
     * well over a minute waiting, so frames are queued instead and the inspector
     * keeps walking. The queue has a single consumer: OCR on a full-resolution
     * frame is memory-hungry, and running several at once on a mid-range phone
     * invites the system to kill the app.
     */
    fun onPhotoTaken(file: File) {
        _state.update { it.copy(processing = it.processing + 1, message = null) }
        frameQueue.trySend(file)
    }

    private suspend fun processFrame(file: File) {
        runCatching {
            val reading = ocr.readFrame(file.path)
            watermark.applyTimestampWatermark(file, _zone.value)
            val relative = fileStore.importCapture(
                Constants.INVENTORY_DIR,
                _zone.value.ifBlank { "Yard" },
                file,
            )
            PendingShot(
                photoAbsolutePath = fileStore.absoluteFor(relative).path,
                photoRelativePath = relative,
                detected = reading.confirmed,
                unread = reading.unread,
            )
        }.onSuccess { shot ->
            Timber.d(
                "Sweep %d: %d confirmed, %d unreadable",
                sweepId, shot.detected.size, shot.unread.size,
            )
            if (shot.detected.isEmpty() && shot.unread.isEmpty()) {
                // Reported rather than shown as a dialog: frames are read behind
                // the inspector now, so a modal would appear several shots later
                // and interrupt a completely different container.
                _state.update {
                    it.copy(
                        message = "One frame read nothing — retake it if that stack matters.",
                        lastShot = AcceptedShot(
                            photoAbsolutePath = shot.photoAbsolutePath,
                            added = emptyList(),
                            duplicates = emptyList(),
                            detected = emptyList(),
                        ),
                    )
                }
            } else {
                accept(shot)
            }
        }.onFailure { e ->
            Timber.e(e, "Sweep capture failed")
            _state.update { it.copy(message = e.message ?: "Could not read the photo") }
        }
        _state.update { it.copy(processing = (it.processing - 1).coerceAtLeast(0)) }
    }

    /** Counts a frame and keeps it on screen so it can be undone. */
    private suspend fun accept(shot: PendingShot) {
        run {
            val already = sightings.value.map { it.containerNumber }.toSet()
            val numbers = shot.detected.map { it.number }
            val duplicates = numbers.filter { it in already }

            repository.addSightings(sweepId, numbers, shot.photoRelativePath, true)

            val tags = repository.addUnread(
                sweepId,
                shot.unread.map { r ->
                    r.partial to floatArrayOf(r.box.left, r.box.top, r.box.right, r.box.bottom)
                },
                shot.photoRelativePath,
            )

            resolveMatching(numbers)

            _state.update {
                it.copy(
                    pending = null,
                    message = null,
                    lastShot = AcceptedShot(
                        photoAbsolutePath = shot.photoAbsolutePath,
                        added = numbers.filterNot { n -> n in already },
                        duplicates = duplicates,
                        detected = shot.detected,
                        needsAttention = tags,
                    ),
                )
            }
        }
    }

    /**
     * Clears any flag whose partial text matches a number just read.
     *
     * Walking closer to photograph A3 usually captures it in a frame of its own;
     * without this the flag would linger after the work was already done.
     */
    private suspend fun resolveMatching(numbers: List<String>) {
        if (numbers.isEmpty()) return
        for (pending in unread.value) {
            val match = numbers.firstOrNull { n ->
                pending.partial.length >= 4 && n.startsWith(pending.partial.take(4))
            }
            if (match != null) repository.resolveUnread(pending.id, match)
        }
    }

    /** Opens the last frame for review — to deselect a misread or look closer. */
    fun reviewLastShot() {
        val last = _state.value.lastShot ?: return
        _state.update {
            it.copy(
                pending = PendingShot(last.photoAbsolutePath, "", last.detected),
                lastShot = null,
            )
        }
    }

    /** Removes everything the last frame added. */
    fun undoLastShot() {
        val last = _state.value.lastShot ?: return
        viewModelScope.launch {
            val current = sightings.value
            last.added.forEach { number ->
                current.firstOrNull { it.containerNumber == number }
                    ?.let { repository.removeSighting(it.id) }
            }
            _state.update { it.copy(lastShot = null) }
        }
    }

    fun dismissLastShot() {
        _state.update { it.copy(lastShot = null) }
    }

    /** Accepts an edited set of numbers from the review sheet. */
    fun confirm(numbers: List<String>) {
        val shot = _state.value.pending ?: return
        viewModelScope.launch {
            val photo = shot.photoRelativePath.ifBlank { null }
            val added = repository.addSightings(sweepId, numbers, photo, true)
            resolveMatching(numbers)
            _state.update {
                it.copy(
                    pending = null,
                    message = if (numbers.isNotEmpty() && added == 0) "Already counted" else null,
                )
            }
        }
    }

    fun discardShot() {
        _state.update { it.copy(pending = null, message = null) }
    }

    /**
     * Clears a flagged unit by typing its number.
     *
     * Rust and glare defeat the camera far more often than they defeat a person
     * standing in front of the container, so this is frequently faster than
     * walking back for another photograph.
     */
    fun resolveUnread(item: UnreadUnit, number: String, onInvalid: () -> Unit) {
        val cleaned = number.trim().uppercase()
        if (validate(cleaned) !is ValidateContainerNumberUseCase.Result.Valid) {
            onInvalid()
            return
        }
        viewModelScope.launch {
            repository.addSightings(sweepId, listOf(cleaned), item.photoPath, false)
            repository.resolveUnread(item.id, cleaned)
        }
    }

    /** Drops a flag — the unit was a duplicate, or is not actually there. */
    fun dismissUnread(item: UnreadUnit) {
        viewModelScope.launch { repository.dismissUnread(item.id) }
    }

    /** Adds a unit the camera could not read at all. */
    fun addManually(number: String, onInvalid: () -> Unit) {
        val cleaned = number.trim().uppercase()
        if (validate(cleaned) !is ValidateContainerNumberUseCase.Result.Valid) {
            onInvalid()
            return
        }
        viewModelScope.launch {
            val added = repository.addSightings(sweepId, listOf(cleaned), null, false)
            resolveMatching(listOf(cleaned))
            _state.update { it.copy(message = if (added == 0) "Already counted" else null) }
        }
    }

    fun remove(sighting: Sighting) {
        viewModelScope.launch { repository.removeSighting(sighting.id) }
    }

    fun finish(onDone: () -> Unit) {
        viewModelScope.launch {
            repository.finishSweep(sweepId)
            // Queued rather than sent: it must survive leaving the yard.
            scheduler.enqueueSweepUpload(sweepId)
            onDone()
        }
    }
}
