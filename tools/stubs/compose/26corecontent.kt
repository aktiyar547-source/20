package androidx.core.content
object ContextCompat {
    fun checkSelfPermission(c: android.content.Context, p: String): Int = 0
    fun getMainExecutor(c: android.content.Context): java.util.concurrent.Executor =
        java.util.concurrent.Executor { it.run() }
    fun startActivity(c: android.content.Context, i: Any?, o: Any?) {}
}
