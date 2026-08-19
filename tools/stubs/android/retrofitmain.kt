package retrofit2
class Retrofit {
    fun <T> create(service: Class<T>): T = throw RuntimeException()
    class Builder {
        fun baseUrl(url: String): Builder = this
        fun client(c: okhttp3.OkHttpClient): Builder = this
        fun addConverterFactory(f: Any?): Builder = this
        fun build(): Retrofit = Retrofit()
    }
}
