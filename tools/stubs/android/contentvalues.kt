package android.content
class ContentValues {
    fun put(k: String, v: String?) {}
    fun put(k: String, v: Int) {}
    fun clear() {}
}
interface ContentResolver {
    fun insert(u: android.net.Uri, v: ContentValues): android.net.Uri?
    fun update(u: android.net.Uri, v: ContentValues, w: String?, a: Array<String>?): Int
    fun delete(u: android.net.Uri, w: String?, a: Array<String>?): Int
    fun openOutputStream(u: android.net.Uri): java.io.OutputStream?
}
