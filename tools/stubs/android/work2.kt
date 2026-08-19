package androidx.work
abstract class CoroutineWorker(appContext: Any?, params: WorkerParameters) {
    abstract suspend fun doWork(): Result
    val inputData: Data = Data()
    val runAttemptCount: Int = 0
    suspend fun setProgress(d: Data) {}
    open class Result { companion object {
        fun success(): Result = Result()
        fun failure(): Result = Result()
        fun retry(): Result = Result() } }
}
class WorkerParameters
