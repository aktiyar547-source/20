package timber.log
object Timber {
    fun plant(tree: Tree) {}
    open class Tree
    class DebugTree : Tree()
    fun d(m: String, vararg a: Any?) {}
    fun i(m: String, vararg a: Any?) {}
    fun w(m: String, vararg a: Any?) {}
    fun w(t: Throwable?, m: String, vararg a: Any?) {}
    fun e(t: Throwable?, m: String, vararg a: Any?) {}
    fun e(m: String, vararg a: Any?) {}
}
