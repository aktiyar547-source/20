package androidx.compose.foundation.layout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
fun Modifier.fillMaxSize(fraction: Float = 1f): Modifier = this
fun Modifier.fillMaxWidth(fraction: Float = 1f): Modifier = this
fun Modifier.fillMaxHeight(fraction: Float = 1f): Modifier = this
fun Modifier.padding(all: Dp): Modifier = this
fun Modifier.padding(horizontal: Dp = Dp(0f), vertical: Dp = Dp(0f)): Modifier = this
fun Modifier.padding(start: Dp = Dp(0f), top: Dp = Dp(0f), end: Dp = Dp(0f), bottom: Dp = Dp(0f)): Modifier = this
fun Modifier.padding(paddingValues: PaddingValues): Modifier = this
fun Modifier.size(size: Dp): Modifier = this
fun Modifier.size(width: Dp, height: Dp): Modifier = this
fun Modifier.height(height: Dp): Modifier = this
fun Modifier.width(width: Dp): Modifier = this
fun Modifier.weight(weight: Float): Modifier = this
class PaddingValues
object Arrangement {
    fun spacedBy(space: Dp): Any = Any()
    val SpaceBetween = Any(); val SpaceAround = Any(); val SpaceEvenly = Any()
    val Center = Any(); val Start = Any(); val End = Any(); val Top = Any(); val Bottom = Any()
}
interface ColumnScope { fun Modifier.weight(weight: Float): Modifier
    fun Modifier.align(a: Alignment.Horizontal): Modifier }
interface RowScope { fun Modifier.weight(weight: Float): Modifier
    fun Modifier.align(a: Alignment.Vertical): Modifier }
interface BoxScope { fun Modifier.align(a: Alignment): Modifier
    fun Modifier.matchParentSize(): Modifier }
@Composable fun Column(modifier: Modifier = Modifier, verticalArrangement: Any? = null,
    horizontalAlignment: Alignment.Horizontal? = null, content: @Composable ColumnScope.() -> Unit) {}
@Composable fun Row(modifier: Modifier = Modifier, horizontalArrangement: Any? = null,
    verticalAlignment: Alignment.Vertical? = null, content: @Composable RowScope.() -> Unit) {}
@Composable fun Box(modifier: Modifier = Modifier, contentAlignment: Alignment? = null,
    content: @Composable BoxScope.() -> Unit) {}
// Real Compose has a content-less overload; several screens use it for spacers.
@Composable fun Box(modifier: Modifier) {}
@Composable fun Spacer(modifier: Modifier = Modifier) {}
