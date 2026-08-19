package okhttp3

class MediaType { object Companion { fun String.toMediaType(): MediaType = MediaType() } }
class ResponseBody
class RequestBody { object Companion { fun String.toRequestBody(t: MediaType? = null): RequestBody = RequestBody() } }

class HttpUrl {
    val encodedPath: String = ""
    fun newBuilder(): Builder = Builder()
    class Builder {
        fun scheme(s: String) = this
        fun host(s: String) = this
        fun port(p: Int) = this
        fun addPathSegments(s: String) = this
        fun build(): HttpUrl = HttpUrl()
    }
    object Companion { fun String.toHttpUrlOrNull(): HttpUrl? = HttpUrl() }
}

class Request {
    val url: HttpUrl = HttpUrl()
    fun newBuilder(): Builder = Builder()
    class Builder { fun url(u: HttpUrl) = this; fun url(u: String) = this; fun build(): Request = Request() }
}

class Response : java.io.Closeable {
    val code: Int = 200
    val isSuccessful: Boolean = true
    override fun close() {}
}

class Call { fun execute(): Response = Response() }

class OkHttpClient {
    fun newBuilder(): Builder = Builder()
    fun newCall(r: Request): Call = Call()
    class Builder {
        fun connectTimeout(t: Long, u: java.util.concurrent.TimeUnit) = this
        fun readTimeout(t: Long, u: java.util.concurrent.TimeUnit) = this
        fun writeTimeout(t: Long, u: java.util.concurrent.TimeUnit) = this
        fun callTimeout(t: Long, u: java.util.concurrent.TimeUnit) = this
        fun addInterceptor(i: Interceptor) = this
        fun build() = OkHttpClient()
    }
}

interface Interceptor {
    fun intercept(chain: Chain): Response
    interface Chain { fun request(): Request; fun proceed(r: Request): Response }
}
