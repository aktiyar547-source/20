package okhttp3.logging
class HttpLoggingInterceptor : okhttp3.Interceptor {
    var level: Level = Level.NONE
    fun redactHeader(name: String) {}
    override fun intercept(chain: okhttp3.Interceptor.Chain): okhttp3.Response = okhttp3.Response()
    enum class Level { NONE, BASIC, HEADERS, BODY }
}
