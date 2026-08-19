package com.middleeastcontainer.ui.inventory

import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import com.middleeastcontainer.domain.ocr.DetectedNumber
import com.middleeastcontainer.domain.ocr.FrameBox
import com.middleeastcontainer.domain.ocr.UnreadRegion
import com.middleeastcontainer.ui.theme.BrandGold
import com.middleeastcontainer.ui.theme.VerifiedGreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * The captured frame with each recognised number boxed.
 *
 * This is the point of keeping the OCR boxes: on a stack of eight containers, a
 * bare list of five numbers does not say which three were missed. Green marks
 * what was read and can be ignored; amber marks what still needs a closer look,
 * labelled so it can be matched to the list.
 */
@Composable
fun DetectionOverlay(
    photoAbsolutePath: String,
    detections: List<DetectedNumber>,
    unread: List<UnreadRegion> = emptyList(),
    unreadTags: List<String> = emptyList(),
    modifier: Modifier = Modifier,
) {
    val bitmapState = produceState<ImageBitmap?>(null, photoAbsolutePath) {
        value = withContext(Dispatchers.IO) {
            val f = File(photoAbsolutePath)
            if (!f.exists()) {
                null
            } else {
                runCatching {
                    val opts = BitmapFactory.Options().apply { inSampleSize = 2 }
                    BitmapFactory.decodeFile(photoAbsolutePath, opts)?.asImageBitmap()
                }.getOrNull()
            }
        }
    }
    val bitmap = bitmapState.value ?: return

    Box(modifier) {
        Image(
            bitmap = bitmap,
            contentDescription = "Captured frame",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
        )

        Canvas(Modifier.fillMaxSize()) {
            // ContentScale.Fit letterboxes the image, so boxes must be mapped
            // through the same fit rather than stretched to the canvas.
            val scale = minOf(
                size.width / bitmap.width.toFloat(),
                size.height / bitmap.height.toFloat(),
            )
            val drawnW = bitmap.width * scale
            val drawnH = bitmap.height * scale
            val offsetX = (size.width - drawnW) / 2f
            val offsetY = (size.height - drawnH) / 2f

            // Boxes are fractions of the image, so they map straight onto the
            // drawn area whatever decode or resize happened in between.
            fun place(box: FrameBox): FloatArray? =
                if (!box.isValid) null
                else floatArrayOf(
                    offsetX + box.left * drawnW,
                    offsetY + box.top * drawnH,
                    offsetX + box.right * drawnW,
                    offsetY + box.bottom * drawnH,
                )

            detections.forEach { d ->
                val p = place(d.box) ?: return@forEach
                drawRect(
                    color = VerifiedGreen,
                    topLeft = Offset(p[0], p[1]),
                    size = Size(p[2] - p[0], p[3] - p[1]),
                    style = Stroke(width = 3f),
                )
            }

            unread.forEachIndexed { i, region ->
                val p = place(region.box) ?: return@forEachIndexed
                drawRect(
                    color = BrandGold,
                    topLeft = Offset(p[0], p[1]),
                    size = Size(p[2] - p[0], p[3] - p[1]),
                    style = Stroke(width = 5f),
                )
                drawRect(
                    color = Color(0x33F2A33C),
                    topLeft = Offset(p[0], p[1]),
                    size = Size(p[2] - p[0], p[3] - p[1]),
                )
                val tag = unreadTags.getOrNull(i) ?: "A${i + 1}"
                val paint = Paint().asFrameworkPaint().apply {
                    isAntiAlias = true
                    textSize = 34f
                    color = android.graphics.Color.WHITE
                    setShadowLayer(6f, 0f, 0f, android.graphics.Color.BLACK)
                    typeface = android.graphics.Typeface.DEFAULT_BOLD
                }
                drawContext.canvas.nativeCanvas.drawText(tag, p[0] + 8f, p[1] + 38f, paint)
            }
        }
    }
}
