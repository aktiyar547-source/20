package retrofit2
class Response<T> {
    val isSuccessful: Boolean = true
    fun code(): Int = 200
    fun message(): String = ""
}
