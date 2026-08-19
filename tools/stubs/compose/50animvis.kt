package androidx.compose.animation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
@Composable fun AnimatedVisibility(visible: Boolean, modifier: Modifier = Modifier,
    enter: EnterTransition = EnterTransition(), exit: ExitTransition = ExitTransition(),
    content: @Composable () -> Unit) {}
