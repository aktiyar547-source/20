package com.middleeastcontainer.ui.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.middleeastcontainer.domain.model.Container
import com.middleeastcontainer.domain.model.ContainerType
import com.middleeastcontainer.domain.repository.ContainerRepository
import com.middleeastcontainer.ui.components.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(
    private val repository: ContainerRepository,
) : ViewModel() {

    val types: List<String> = ContainerType.wireValues

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    /**
     * Results for the current search.
     *
     * Debounced and de-duplicated so a typed number issues one query rather than
     * eleven, and flatMapLatest cancels a search the moment it is superseded.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val state = _query
        .debounce { if (it.isEmpty()) 0L else SEARCH_DEBOUNCE_MS }
        .distinctUntilChanged()
        .flatMapLatest { repository.search(it) }
        .map { list -> if (list.isEmpty()) UiState.Empty else UiState.Content(list) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading)

    fun onQueryChange(value: String) {
        _query.value = value
    }

    fun clearQuery() {
        _query.value = ""
    }

    fun updateType(container: String, type: String) = viewModelScope.launch {
        repository.updateType(container, type)
    }

    private companion object {
        /** Long enough to skip intermediate keystrokes, short enough to feel live. */
        const val SEARCH_DEBOUNCE_MS = 250L
    }
}
