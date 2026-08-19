package android.content
class Intent(action: String? = null, uri: android.net.Uri? = null) {
    var type: String? = null
    fun putExtra(name: String, value: String?): Intent = this
    fun putExtra(name: String, value: android.net.Uri?): Intent = this
    fun addFlags(flags: Int): Intent = this
    companion object {
        const val ACTION_SEND = "android.intent.action.SEND"
        const val EXTRA_STREAM = "android.intent.extra.STREAM"
        const val EXTRA_SUBJECT = "android.intent.extra.SUBJECT"
        const val FLAG_GRANT_READ_URI_PERMISSION = 1
        fun createChooser(target: Intent, title: String?): Intent = Intent()
    }
}
