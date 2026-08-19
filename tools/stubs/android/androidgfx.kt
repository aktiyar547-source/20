package android.graphics
class Bitmap {
    val width: Int = 0
    val height: Int = 0
    fun copy(c: Config, m: Boolean): Bitmap = this
    fun compress(f: CompressFormat, q: Int, s: java.io.OutputStream): Boolean = true
    fun recycle() {}
    enum class Config { ARGB_8888 }
    enum class CompressFormat { JPEG, PNG }
    companion object {
        fun createBitmap(b: Bitmap, x: Int, y: Int, w: Int, h: Int, m: Matrix, f: Boolean): Bitmap = b
        fun createScaledBitmap(b: Bitmap, w: Int, h: Int, f: Boolean): Bitmap = b
    }
}
object BitmapFactory {
    class Options { var inSampleSize: Int = 1; var inJustDecodeBounds: Boolean = false
        var outWidth: Int = 0; var outHeight: Int = 0 }
    fun decodeFile(p: String, o: Options? = null): Bitmap? = null
}
class Matrix { fun preRotate(d: Float) {}; fun postRotate(d: Float) {} }
class Canvas(b: Bitmap) {
    fun drawRect(l: Float, t: Float, r: Float, b2: Float, p: Paint) {}
    fun drawText(s: String, x: Float, y: Float, p: Paint) {}
}
class Typeface { companion object { val DEFAULT_BOLD = Typeface(); val DEFAULT = Typeface() } }
class Paint { var color: Int = 0; var textSize: Float = 0f; var isAntiAlias = false
    var typeface: Typeface? = null
    fun setShadowLayer(r: Float, dx: Float, dy: Float, c: Int) {}
    var isFakeBoldText = false; var textAlign: Align = Align.LEFT
    enum class Align { LEFT, RIGHT } }
object Color { const val WHITE = -1; const val RED = -65536; const val BLACK = -16777216
    fun argb(a: Int, r: Int, g: Int, b: Int): Int = 0 }
class Rect(val left: Int, val top: Int, val right: Int, val bottom: Int) {
    fun width(): Int = right - left
    fun height(): Int = bottom - top
    fun centerX(): Int = (left + right) / 2
}
