package com.middleeastcontainer.ui.menu

import androidx.lifecycle.ViewModel
import com.middleeastcontainer.data.storage.ImageFileStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/** Free space where photos are written. */
data class StorageState(
    val freeMb: Long = 0,
    val low: Boolean = false,
)

/**
 * Menu-level state.
 *
 * Its one job is the storage warning. A full disk otherwise announces itself as
 * a failed write halfway round a yard, when the inspector can do nothing about
 * it; the menu is the last point where they can still upload and clear space
 * before walking out.
 */
@HiltViewModel
class MenuViewModel @Inject constructor(
    private val fileStore: ImageFileStore,
) : ViewModel() {

    private val _storage = MutableStateFlow(StorageState())
    val storage = _storage.asStateFlow()

    /** Re-read whenever the menu is shown; space changes as photos upload and purge. */
    fun refreshStorage() {
        _storage.value = StorageState(
            freeMb = fileStore.freeSpaceMb(),
            low = fileStore.isLowOnSpace(),
        )
    }
}
