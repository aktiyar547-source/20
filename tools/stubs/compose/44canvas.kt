package androidx.compose.ui.graphics
class Canvas
val Canvas.nativeCanvas: android.graphics.Canvas
    get() = android.graphics.Canvas(android.graphics.Bitmap())
