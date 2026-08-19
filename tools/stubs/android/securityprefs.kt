package androidx.security.crypto
class MasterKey { class Builder(c: Any?) { fun setKeyScheme(s: Any?) = this; fun build() = MasterKey() }
    enum class KeyScheme { AES256_GCM } }
object EncryptedSharedPreferences {
    fun create(c: Any?, n: String, k: MasterKey, a: Any?, b: Any?): android.content.SharedPreferences =
        throw RuntimeException()
    enum class PrefKeyEncryptionScheme { AES256_SIV }
    enum class PrefValueEncryptionScheme { AES256_GCM }
}
