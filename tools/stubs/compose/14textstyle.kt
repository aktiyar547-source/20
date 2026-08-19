package androidx.compose.ui.text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
class TextStyle(
    val color: Color = Color.Unspecified,
    val fontSize: TextUnit? = null,
    val fontWeight: Any? = null,
    val fontFamily: Any? = null,
    val letterSpacing: TextUnit? = null,
    val lineHeight: TextUnit? = null,
    val textAlign: Any? = null,
) {
    fun copy(
        color: Color = this.color,
        fontSize: TextUnit? = this.fontSize,
        fontWeight: Any? = this.fontWeight,
        fontFamily: Any? = this.fontFamily,
        letterSpacing: TextUnit? = this.letterSpacing,
        lineHeight: TextUnit? = this.lineHeight,
    ): TextStyle = this
}
