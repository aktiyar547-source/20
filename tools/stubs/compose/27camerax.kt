package androidx.camera.core
class Camera { val cameraInfo: CameraInfo = CameraInfo(); val cameraControl: CameraControl = CameraControl() }
class CameraInfo { val zoomState: androidx.lifecycle.LiveData<ZoomState> = androidx.lifecycle.LiveData() }
class ZoomState { val zoomRatio: Float = 1f }
class CameraControl {
    fun setZoomRatio(r: Float) {}
    fun enableTorch(on: Boolean) {}
    fun startFocusAndMetering(a: FocusMeteringAction): Any? = null
}
class CameraSelector { companion object { val DEFAULT_BACK_CAMERA = CameraSelector() } }
class FocusMeteringAction { class Builder(p: Any?, m: Int) {
    fun setAutoCancelDuration(d: Long, u: java.util.concurrent.TimeUnit) = this
    fun build() = FocusMeteringAction() }
    companion object { const val FLAG_AF = 1 } }
class ImageCapture {
    fun takePicture(o: OutputFileOptions, e: java.util.concurrent.Executor, c: OnImageSavedCallback) {}
    class Builder { fun setCaptureMode(m: Int) = this; fun build() = ImageCapture() }
    class OutputFileOptions { class Builder(f: java.io.File) { fun build() = OutputFileOptions() } }
    class OutputFileResults
    interface OnImageSavedCallback {
        fun onImageSaved(r: OutputFileResults)
        fun onError(e: ImageCaptureException)
    }
    companion object { const val CAPTURE_MODE_MINIMIZE_LATENCY = 1
        const val CAPTURE_MODE_MAXIMIZE_QUALITY = 0 }
}
class ImageCaptureException(override val message: String?) : Exception(message)
class Preview { fun setSurfaceProvider(p: Any?) {}
    class Builder { fun build() = Preview() } }
