package androidx.compose.ui.graphics
class Color(v: Long = 0) {
    fun copy(alpha: Float = 1f, red: Float = 0f, green: Float = 0f, blue: Float = 0f) = this
    companion object {
        val White = Color(); val Black = Color(); val Transparent = Color()
        val Red = Color(); val Gray = Color(); val Unspecified = Color()
    }
}
class ImageBitmap { val width: Int = 0; val height: Int = 0 }
class Paint { fun asFrameworkPaint(): android.graphics.Paint = android.graphics.Paint() }
class Typeface
class Brush { companion object { fun verticalGradient(colors: List<Color>): Brush = Brush()
    fun horizontalGradient(colors: List<Color>): Brush = Brush() } }
interface Shape
fun android.graphics.Bitmap.asImageBitmap(): ImageBitmap = ImageBitmap()

class GraphicsLayerScope { var scaleX = 1f; var scaleY = 1f; var alpha = 1f
    var rotationZ = 0f; var translationY = 0f }
fun androidx.compose.ui.Modifier.graphicsLayer(block: GraphicsLayerScope.() -> Unit): androidx.compose.ui.Modifier = this
