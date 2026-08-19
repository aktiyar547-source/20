package androidx.activity.compose
import androidx.compose.runtime.Composable
class ManagedActivityResultLauncher<I, O> { fun launch(input: I) {} }
@Composable fun <I, O> rememberLauncherForActivityResult(
    contract: androidx.activity.result.contract.ActivityResultContract<I, O>,
    onResult: (O) -> Unit,
): ManagedActivityResultLauncher<I, O> = ManagedActivityResultLauncher()
fun androidx.activity.ComponentActivity.setContent(content: @Composable () -> Unit) {}
