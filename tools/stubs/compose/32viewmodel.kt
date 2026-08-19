package androidx.lifecycle
open class ViewModel { open fun onCleared() {} }
class SavedStateHandle {
    operator fun <T> get(key: String): T? = null
    operator fun <T> set(key: String, value: T) {}
}
val ViewModel.viewModelScope: kotlinx.coroutines.CoroutineScope
    get() = throw RuntimeException()
