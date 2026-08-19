package androidx.compose.ui
interface Modifier {
    companion object : Modifier
    infix fun then(other: Modifier): Modifier = this
}
class Alignment {
    companion object {
        val Center = Alignment(); val CenterStart = Alignment(); val CenterEnd = Alignment()
        val TopStart = Alignment(); val TopEnd = Alignment(); val TopCenter = Alignment()
        val BottomStart = Alignment(); val BottomEnd = Alignment(); val BottomCenter = Alignment()
        val CenterHorizontally = Horizontal(); val Start = Horizontal(); val End = Horizontal()
        val CenterVertically = Vertical(); val Top = Vertical(); val Bottom = Vertical()
    }
    class Horizontal
    class Vertical
}
