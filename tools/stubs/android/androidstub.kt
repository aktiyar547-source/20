package android.content
// java.io.File.usableSpace exists on the JVM already; nothing to stub.
interface Context {
    fun getExternalFilesDir(t: String?): java.io.File?
    val packageName: String
    val contentResolver: ContentResolver
}
interface SharedPreferences {
    fun getString(k: String, d: String?): String?
    fun getBoolean(k: String, d: Boolean): Boolean
    fun edit(): Editor
    fun registerOnSharedPreferenceChangeListener(l: OnSharedPreferenceChangeListener)
    fun unregisterOnSharedPreferenceChangeListener(l: OnSharedPreferenceChangeListener)
    fun interface OnSharedPreferenceChangeListener {
        fun onSharedPreferenceChanged(p: SharedPreferences?, k: String?)
    }
    interface Editor {
        fun putString(k: String, v: String?): Editor
        fun putBoolean(k: String, v: Boolean): Editor
        fun remove(k: String): Editor
        fun apply()
    }
}
