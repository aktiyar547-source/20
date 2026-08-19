package android.os
object Build { object VERSION { const val SDK_INT = 33 }
    object VERSION_CODES { const val R = 30 } }
object Environment {
    const val DIRECTORY_DOWNLOADS = "Download"
    fun getExternalStorageDirectory(): java.io.File = java.io.File("/storage/emulated/0")
    fun isExternalStorageManager(): Boolean = true
}
