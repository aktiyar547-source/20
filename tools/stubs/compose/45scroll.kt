package androidx.compose.foundation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
class ScrollState
@Composable fun rememberScrollState(): ScrollState = ScrollState()
fun Modifier.verticalScroll(state: ScrollState): Modifier = this
fun Modifier.horizontalScroll(state: ScrollState): Modifier = this
