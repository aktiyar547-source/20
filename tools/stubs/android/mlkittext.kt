package com.google.mlkit.vision.text
class Text {
    val text: String = ""
    val textBlocks: List<TextBlock> = emptyList()
    class TextBlock { val text: String = ""; val lines: List<Line> = emptyList()
        val boundingBox: android.graphics.Rect? = null }
    class Line { val text: String = ""; val elements: List<Element> = emptyList()
        val boundingBox: android.graphics.Rect? = null }
    class Element { val text: String = ""; val boundingBox: android.graphics.Rect? = null }
}
interface TextRecognizer { fun process(i: com.google.mlkit.vision.common.InputImage): Task<Text> }
class Task<T> {
    fun addOnSuccessListener(l: (T) -> Unit): Task<T> = this
    fun addOnFailureListener(l: (Exception) -> Unit): Task<T> = this
}
object TextRecognition {
    fun getClient(o: Any?): TextRecognizer = throw RuntimeException()
}
