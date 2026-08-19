package androidx.compose.ui.graphics.drawscope
class Stroke(val width: Float = 1f)
interface DrawScope {
    val size: androidx.compose.ui.geometry.Size
    val drawContext: DrawContext
    fun drawRect(color: androidx.compose.ui.graphics.Color,
                 topLeft: androidx.compose.ui.geometry.Offset,
                 size: androidx.compose.ui.geometry.Size,
                 style: Any? = null)
}
class DrawContext { val canvas: androidx.compose.ui.graphics.Canvas = androidx.compose.ui.graphics.Canvas() }

