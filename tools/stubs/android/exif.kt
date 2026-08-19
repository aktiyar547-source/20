package android.media
class ExifInterface(path: String) {
    fun getAttributeInt(tag: String, d: Int): Int = d
    companion object {
        const val TAG_ORIENTATION = "Orientation"
        const val ORIENTATION_NORMAL = 1
        const val ORIENTATION_ROTATE_90 = 6
        const val ORIENTATION_ROTATE_180 = 3
        const val ORIENTATION_ROTATE_270 = 8
    }
}
