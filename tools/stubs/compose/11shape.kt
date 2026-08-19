package androidx.compose.foundation.shape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
class RoundedCornerShapeImpl : Shape
fun RoundedCornerShape(size: Dp): Shape = RoundedCornerShapeImpl()
fun RoundedCornerShape(topStart: Dp = Dp(0f), topEnd: Dp = Dp(0f),
    bottomEnd: Dp = Dp(0f), bottomStart: Dp = Dp(0f)): Shape = RoundedCornerShapeImpl()
val CircleShape: Shape = RoundedCornerShapeImpl()
