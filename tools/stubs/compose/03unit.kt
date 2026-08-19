package androidx.compose.ui.unit
class Dp(val value: Float) { operator fun times(o: Int) = Dp(value * o) }
val Int.dp: Dp get() = Dp(this.toFloat())
val Double.dp: Dp get() = Dp(this.toFloat())
class TextUnit { operator fun times(o: Float) = TextUnit() }
val Int.sp: TextUnit get() = TextUnit()
val Double.sp: TextUnit get() = TextUnit()
val Float.sp: TextUnit get() = TextUnit()
val Int.em: TextUnit get() = TextUnit()
val Double.em: TextUnit get() = TextUnit()
