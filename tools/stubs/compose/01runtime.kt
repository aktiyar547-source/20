package androidx.compose.runtime
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.TYPE,
    AnnotationTarget.TYPE_PARAMETER,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.VALUE_PARAMETER,
)
@Retention(AnnotationRetention.BINARY)
annotation class Composable
annotation class Stable
annotation class Immutable
annotation class ReadOnlyComposable
interface State<T> { val value: T }
interface MutableState<T> : State<T> { override var value: T }
fun <T> mutableStateOf(v: T): MutableState<T> = object : MutableState<T> { override var value = v }
fun <T> mutableStateListOf(): SnapshotStateList<T> = SnapshotStateList()
class SnapshotStateList<T> : MutableList<T> by mutableListOf()
@Composable fun <T> remember(calc: () -> T): T = calc()
@Composable fun <T> remember(k1: Any?, calc: () -> T): T = calc()
@Composable fun <T> remember(k1: Any?, k2: Any?, calc: () -> T): T = calc()
@Composable fun LaunchedEffect(key1: Any?, block: suspend kotlinx.coroutines.CoroutineScope.() -> Unit) {}
@Composable fun DisposableEffect(key1: Any?, effect: DisposableEffectScope.() -> DisposableEffectResult) {}
class DisposableEffectScope { fun onDispose(f: () -> Unit): DisposableEffectResult = DisposableEffectResult() }
class DisposableEffectResult
@Composable fun <T> produceState(initialValue: T, key1: Any?, producer: suspend ProduceStateScope<T>.() -> Unit): State<T> =
    object : State<T> { override val value = initialValue }
interface ProduceStateScope<T> { var value: T }
operator fun <T> State<T>.getValue(t: Any?, p: Any?): T = value
operator fun <T> MutableState<T>.setValue(t: Any?, p: Any?, v: T) { value = v }
open class ProvidableCompositionLocal<T> { val current: T @Composable get() = throw RuntimeException() }
@Composable fun rememberCoroutineScope(): kotlinx.coroutines.CoroutineScope = throw RuntimeException()

fun <K, V> mutableStateMapOf(): MutableMap<K, V> = mutableMapOf()
