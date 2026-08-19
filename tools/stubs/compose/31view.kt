package android.view
open class View { fun performClick(): Boolean = true }
open class MotionEvent { val action: Int = 0; val x: Float = 0f; val y: Float = 0f
    companion object { const val ACTION_UP = 1; const val ACTION_DOWN = 0 } }
class ScaleGestureDetector(c: android.content.Context, l: Any?) {
    val isInProgress: Boolean = false
    val scaleFactor: Float = 1f
    fun onTouchEvent(e: MotionEvent): Boolean = true
    open class SimpleOnScaleGestureListener { open fun onScale(d: ScaleGestureDetector): Boolean = true }
}
