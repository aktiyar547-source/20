package com.middleeastcontainer.data.storage

import android.content.Context
import android.os.Build
import android.os.Environment
import com.middleeastcontainer.core.common.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Where inspection and inventory photos live on the phone.
 *
 * Preferred layout, at the top of shared storage so it is obvious over USB or in
 * any file manager:
 *
 *     /OCR2/Inspection/<yyyy>/<MM>/<yyyy-MM-dd>/<container>/
 *     /OCR2/Inventory/<yyyy>/<MM>/<yyyy-MM-dd>/<zone>/
 *
 * Android has not allowed apps to write to the storage root since API 29, so this
 * needs the All-files-access permission. That is granted once in Settings, and is
 * available here because the app is distributed directly rather than through the
 * Play Store.
 *
 * Without it, photos fall back to the app's private directory. Everything keeps
 * working — the files are simply buried under Android/data and awkward to reach.
 */
@Singleton
class ImageFileStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    /** True when photos can be written to the visible /OCR2 folder. */
    val hasRootAccess: Boolean
        get() = Build.VERSION.SDK_INT < Build.VERSION_CODES.R || Environment.isExternalStorageManager()

    /** Root actually in use, which may be the private fallback. */
    val root: File
        get() = if (hasRootAccess) sharedRoot() else privateRoot()

    /** Where the visible folder would be, whether or not it is reachable yet. */
    /**
     * Creates /OCR2 and its two sections straight away.
     *
     * Called as soon as the permission is granted rather than waiting for the
     * first photo: an inspector who has just granted access should be able to
     * plug the phone in and see the folder. An empty folder is also the clearest
     * possible confirmation that the permission actually took effect.
     *
     * @return true when the folders exist and are writable.
     */
    fun ensureFolders(): Boolean {
        if (!hasRootAccess) return false
        return runCatching {
            val base = sharedRoot()
            val inspection = File(base, Constants.INSPECTION_DIR).apply { mkdirs() }
            val inventory = File(base, Constants.INVENTORY_DIR).apply { mkdirs() }
            val exports = File(base, Constants.EXPORT_DIR).apply { mkdirs() }

            // Creating a directory is not proof of being able to write into it:
            // some builds allow the folder and then refuse the file. Prove it
            // with a real write, so Settings cannot report success on a device
            // that will lose the first photo.
            val probe = File(inspection, ".write-test")
            val writable = runCatching {
                probe.writeText("ok")
                val readable = probe.readText() == "ok"
                probe.delete()
                readable
            }.getOrDefault(false)

            val ok = inspection.isDirectory && inventory.isDirectory &&
                exports.isDirectory && writable
            if (ok) Timber.i("Photo folders ready at %s", base.path)
            else Timber.w("Could not create photo folders at %s", base.path)
            ok
        }.getOrElse {
            Timber.w(it, "Could not create photo folders")
            false
        }
    }

    /**
     * Folder for generated spreadsheets, or null when there is no visible root.
     *
     * Sheets sit beside the photos they describe rather than in Downloads, so a
     * yard's paperwork stays in one place when the phone is plugged in.
     */
    fun exportDir(): File? {
        if (!hasRootAccess) return null
        return File(sharedRoot(), Constants.EXPORT_DIR).apply { if (!exists()) mkdirs() }
    }

    fun sharedRootPath(): String =
        File(Environment.getExternalStorageDirectory(), Constants.IMAGE_ROOT_DIR).path

    private fun sharedRoot(): File =
        File(Environment.getExternalStorageDirectory(), Constants.IMAGE_ROOT_DIR)
            .apply { if (!exists()) mkdirs() }

    private fun privateRoot(): File =
        File(context.getExternalFilesDir(null), Constants.IMAGE_ROOT_DIR)
            .apply { if (!exists()) mkdirs() }

    /**
     * Resolves a stored path.
     *
     * Photos captured before the all-files permission was granted live under the
     * private root, and their database rows are relative to that. Once the
     * permission is granted the root changes, so resolving only against the
     * current root would make every earlier photo silently disappear. Both are
     * checked, newest location first.
     */
    /** Free space where photos are written, in megabytes. */
    fun freeSpaceMb(): Long = runCatching { root.usableSpace / (1024 * 1024) }.getOrDefault(0L)

    /**
     * Whether there is room to keep capturing.
     *
     * A full disk shows up as a failed write in the middle of a yard, long after
     * the inspector could have done anything about it. Warning early is the only
     * useful moment: [LOW_SPACE_MB] leaves room for roughly a hundred more
     * photos, which is a container inspection or a decent sweep.
     */
    fun isLowOnSpace(): Boolean = freeSpaceMb() < LOW_SPACE_MB

    fun absoluteFor(relativePath: String): File {
        val primary = File(root, relativePath)
        if (primary.exists()) return primary
        val fallback = File(otherRoot(), relativePath)
        return if (fallback.exists()) fallback else primary
    }

    /** The root not currently in use — where older photos may still sit. */
    private fun otherRoot(): File = if (hasRootAccess) privateRoot() else sharedRoot()

    /**
     * Allocates a new photo path.
     *
     * @param section [Constants.INSPECTION_DIR] or [Constants.INVENTORY_DIR]
     * @param group container number, or zone for a sweep
     * @return the RELATIVE path stored in the database. Relative on purpose: the
     *   absolute root changes when the permission is granted, and rows written
     *   before that must not break.
     */
    fun newCaptureFile(section: String, group: String): Pair<String, File> {
        val relDir = FolderPathBuilder.relativeDir(section, group)
        val fileName = FolderPathBuilder.captureFileName(group)
        val relPath = "$relDir/$fileName"

        // mkdirs() returns false rather than throwing, and some manufacturer
        // builds — MIUI in particular — refuse writes at the top of shared
        // storage even with all-files access granted. Left unchecked, the copy
        // then fails with ENOENT and the photo is simply lost. Verify, and fall
        // back to app-private storage rather than lose the capture.
        val preferred = File(root, relDir)
        if (preferred.isDirectory || preferred.mkdirs()) {
            return relPath to File(root, relPath)
        }

        Timber.w("Could not create %s — falling back to private storage", preferred.path)
        val fallback = File(privateRoot(), relDir)
        fallback.mkdirs()
        return relPath to File(privateRoot(), relPath)
    }

    /**
     * Recursively deletes one group's photos, across every date folder and both
     * roots.
     *
     * Folders are dated, so looking only under today's date would miss anything
     * captured earlier — the database row would go and the files would remain on
     * the phone forever.
     */
    fun deleteGroupDir(section: String, group: String) {
        val target = FolderPathBuilder.safeName(group)
        for (base in listOf(root, otherRoot())) {
            val sectionDir = File(base, FolderPathBuilder.safeName(section))
            if (!sectionDir.isDirectory) continue
            // <section>/<yyyy>/<MM>/<yyyy-MM-dd>/<group>
            sectionDir.listFiles()?.forEach { year ->
                year.listFiles()?.forEach { month ->
                    month.listFiles()?.forEach { day ->
                        File(day, target).takeIf { it.isDirectory }?.deleteRecursively()
                    }
                }
            }
        }
    }

    /** Deletes every photo belonging to one group, by relative path. */
    fun deleteAll(relativePaths: List<String>) {
        relativePaths.forEach { deleteRelative(it) }
    }

    /** Deletes one stored image by its DB-relative path. Silent if already gone. */
    fun deleteRelative(relativePath: String) {
        runCatching { File(root, relativePath).delete() }
        runCatching { File(otherRoot(), relativePath).delete() }
    }

    /** Converts an absolute path under the store back to the RELATIVE path kept in the DB. */
    fun relativeOf(absolutePath: String): String {
        val rootPath = root.path
        return if (absolutePath.startsWith(rootPath)) {
            absolutePath.removePrefix(rootPath).trimStart('/')
        } else {
            absolutePath
        }
    }

    /**
     * Moves a freshly captured (already watermarked) file into place and returns
     * the RELATIVE path stored in the DB. The source is removed.
     */
    fun importCapture(section: String, group: String, source: File): String {
        val (relPath, target) = newCaptureFile(section, group)
        source.copyTo(target, overwrite = true)

        // Only discard the original once the copy is on disk and non-empty.
        // Deleting first would turn a storage refusal into a lost photograph,
        // and the inspector would have walked to that container for nothing.
        if (!target.isFile || target.length() == 0L) {
            error("Could not write ${target.path}")
        }
        source.delete()
        Timber.d("Stored %s (%d bytes)", relPath, target.length())
        return relPath
    }

    private companion object {
        /**
         * Free megabytes below which capture is considered at risk.
         *
         * About a hundred photos at the current storage size — an inspection or
         * a short sweep — so the warning arrives while there is still time to
         * upload and clear space rather than at the moment a write fails.
         */
        const val LOW_SPACE_MB = 200L
    }
}
