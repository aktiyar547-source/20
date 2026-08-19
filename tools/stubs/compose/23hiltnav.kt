package androidx.hilt.navigation.compose
import androidx.compose.runtime.Composable
@Composable inline fun <reified VM> hiltViewModel(): VM = throw RuntimeException()
