package android.app
open class Application : android.content.Context {
    override fun getExternalFilesDir(t: String?): java.io.File? = null
    override val packageName: String = ""
    override val contentResolver: android.content.ContentResolver
        get() = throw RuntimeException()
    open fun onCreate() {}
}
