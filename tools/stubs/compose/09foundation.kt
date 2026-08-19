package androidx.compose.foundation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
annotation class ExperimentalFoundationApi
fun Modifier.background(color: Color, shape: Shape? = null): Modifier = this
fun Modifier.background(brush: Brush, shape: Shape? = null): Modifier = this
fun Modifier.border(width: Dp, color: Color, shape: Shape? = null): Modifier = this
fun Modifier.clickable(enabled: Boolean = true, onClick: () -> Unit): Modifier = this
fun Modifier.combinedClickable(enabled: Boolean = true, onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null): Modifier = this
@Composable fun Image(bitmap: ImageBitmap, contentDescription: String?, modifier: Modifier = Modifier,
    contentScale: Any? = null) {}
@Composable fun Canvas(modifier: Modifier, onDraw: androidx.compose.ui.graphics.drawscope.DrawScope.() -> Unit) {}
