package kotlinx.serialization.json

/**
 * Mirrors the real API deliberately: encodeToString(serializer, value) is a
 * member, while the single-argument reified form is an extension in the
 * kotlinx.serialization package and needs its own import. A stub that offered
 * both as members hid a genuine compile error.
 */
open class Json {
    var ignoreUnknownKeys: Boolean = false
    var encodeDefaults: Boolean = false
    fun <T> encodeToString(serializer: Any?, value: T): String = ""
    companion object : Json()
}

fun Json(from: Json = Json, builder: Json.() -> Unit): Json = Json().apply(builder)
