package android.provider
object Settings {
    const val ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION =
        "android.settings.MANAGE_APP_ALL_FILES_ACCESS_PERMISSION"
}
object MediaStore {
    object Downloads {
        const val DISPLAY_NAME = "_display_name"
        const val MIME_TYPE = "mime_type"
        const val IS_PENDING = "is_pending"
        const val RELATIVE_PATH = "relative_path"
        val EXTERNAL_CONTENT_URI: android.net.Uri = android.net.Uri()
    }
}
