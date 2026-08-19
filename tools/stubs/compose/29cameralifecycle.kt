package androidx.camera.lifecycle
import androidx.camera.core.*
class ProcessCameraProvider {
    fun unbindAll() {}
    fun bindToLifecycle(owner: androidx.lifecycle.LifecycleOwner, selector: CameraSelector,
        vararg useCases: Any): Camera = Camera()
    companion object { fun getInstance(c: android.content.Context): Future = Future() }
    class Future { fun addListener(r: Runnable, e: java.util.concurrent.Executor) {}
        fun get(): ProcessCameraProvider = ProcessCameraProvider() }
}
