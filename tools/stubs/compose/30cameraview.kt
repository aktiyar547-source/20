package androidx.camera.view
class PreviewView(context: android.content.Context) {
    val surfaceProvider: Any? = null
    val meteringPointFactory: MeteringPointFactory = MeteringPointFactory()
    fun setOnTouchListener(l: (android.view.View, android.view.MotionEvent) -> Boolean) {}
    fun performClick(): Boolean = true
}
class MeteringPointFactory { fun createPoint(x: Float, y: Float): Any = Any() }
