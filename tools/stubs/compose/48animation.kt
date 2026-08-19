package androidx.compose.animation.core
class CubicBezierEasing(a: Float, b: Float, c: Float, d: Float) : Easing
interface Easing
interface FiniteAnimationSpec<T>
class TweenSpec<T> : FiniteAnimationSpec<T>
fun <T> tween(durationMillis: Int = 300, delayMillis: Int = 0, easing: Easing? = null): FiniteAnimationSpec<T> = TweenSpec()
fun <T> spring(dampingRatio: Float = 1f, stiffness: Float = 1500f): FiniteAnimationSpec<T> = TweenSpec()
object Spring {
    const val DampingRatioMediumBouncy = 0.5f
    const val DampingRatioNoBouncy = 1f
    const val StiffnessMedium = 1500f
    const val StiffnessLow = 200f
}

@androidx.compose.runtime.Composable
fun animateFloatAsState(targetValue: Float, animationSpec: FiniteAnimationSpec<Float>? = null,
    label: String = ""): androidx.compose.runtime.State<Float> =
    object : androidx.compose.runtime.State<Float> { override val value = targetValue }
