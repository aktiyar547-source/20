package androidx.compose.animation
import androidx.compose.animation.core.FiniteAnimationSpec
class EnterTransition { operator fun plus(o: EnterTransition) = this }
class ExitTransition { operator fun plus(o: ExitTransition) = this }
fun fadeIn(animationSpec: FiniteAnimationSpec<Float>? = null): EnterTransition = EnterTransition()
fun fadeOut(animationSpec: FiniteAnimationSpec<Float>? = null): ExitTransition = ExitTransition()
fun slideInHorizontally(initialOffsetX: (Int) -> Int = { it },
    animationSpec: FiniteAnimationSpec<Int>? = null): EnterTransition = EnterTransition()
fun slideOutHorizontally(targetOffsetX: (Int) -> Int = { it },
    animationSpec: FiniteAnimationSpec<Int>? = null): ExitTransition = ExitTransition()
fun slideInVertically(initialOffsetY: (Int) -> Int = { it },
    animationSpec: FiniteAnimationSpec<Int>? = null): EnterTransition = EnterTransition()
fun slideOutVertically(targetOffsetY: (Int) -> Int = { it },
    animationSpec: FiniteAnimationSpec<Int>? = null): ExitTransition = ExitTransition()
fun expandVertically(animationSpec: FiniteAnimationSpec<Int>? = null): EnterTransition = EnterTransition()
fun shrinkVertically(animationSpec: FiniteAnimationSpec<Int>? = null): ExitTransition = ExitTransition()
