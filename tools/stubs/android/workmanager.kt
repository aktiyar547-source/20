package androidx.work
class Data {
    fun getLong(k: String, d: Long): Long = d
    fun getString(k: String): String? = null
    fun getInt(k: String, d: Int): Int = d
    class Builder { fun putString(k: String, v: String?) = this
    fun putLong(k: String, v: Long) = this
    fun build() = Data() } }
class Constraints { class Builder { fun setRequiredNetworkType(t: Int) = this; fun build() = Constraints() } }
object NetworkType { const val CONNECTED = 1 }
enum class BackoffPolicy { EXPONENTIAL, LINEAR }
enum class ExistingWorkPolicy { KEEP, REPLACE, APPEND }
enum class ExistingPeriodicWorkPolicy { KEEP, UPDATE }
class WorkInfo { enum class State { ENQUEUED, RUNNING, SUCCEEDED, FAILED, BLOCKED, CANCELLED }
    val state: State = State.ENQUEUED
    val tags: Set<String> = emptySet()
    val progress: Data = Data() }
class OneTimeWorkRequest
inline fun <reified T> OneTimeWorkRequestBuilder(): WorkRequestBuilder = WorkRequestBuilder()
inline fun <reified T> PeriodicWorkRequestBuilder(a: Long, b: java.util.concurrent.TimeUnit): WorkRequestBuilder = WorkRequestBuilder()
class WorkRequestBuilder {
    fun setInputData(d: Data) = this
    fun setConstraints(c: Constraints) = this
    fun setBackoffCriteria(p: BackoffPolicy, a: Long, u: java.util.concurrent.TimeUnit) = this
    fun addTag(t: String) = this
    fun setInitialDelay(a: Long, u: java.util.concurrent.TimeUnit) = this
    fun build(): OneTimeWorkRequest = OneTimeWorkRequest()
}
class PeriodicWorkRequest
class WorkManager {
    fun enqueueUniqueWork(n: String, p: ExistingWorkPolicy, r: Any) {}
    fun enqueueUniquePeriodicWork(n: String, p: ExistingPeriodicWorkPolicy, r: Any) {}
    fun getWorkInfosByTagFlow(t: String): kotlinx.coroutines.flow.Flow<List<WorkInfo>> =
        kotlinx.coroutines.flow.flowOf(emptyList())
    companion object { fun getInstance(c: Any?): WorkManager = WorkManager() }
}

fun workDataOf(vararg pairs: Pair<String, Any?>): Data = Data()
