package androidx.lifecycle.compose
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.Lifecycle
import androidx.compose.runtime.ProvidableCompositionLocal
@Composable fun <T> kotlinx.coroutines.flow.StateFlow<T>.collectAsStateWithLifecycle(): State<T> =
    object : State<T> { override val value = this@collectAsStateWithLifecycle.value }
@Composable fun LifecycleEventEffect(event: Lifecycle.Event, onEvent: () -> Unit) {}
val LocalLifecycleOwner = object : ProvidableCompositionLocal<androidx.lifecycle.LifecycleOwner>() {}
