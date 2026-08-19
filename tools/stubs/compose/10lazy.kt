package androidx.compose.foundation.lazy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
interface LazyItemScope
interface LazyListScope {
    fun item(content: @Composable LazyItemScope.() -> Unit)
    fun items(count: Int, itemContent: @Composable LazyItemScope.(Int) -> Unit)
}
fun <T> LazyListScope.items(items: List<T>, itemContent: @Composable LazyItemScope.(T) -> Unit) {}
fun <T> LazyListScope.itemsIndexed(items: List<T>, itemContent: @Composable LazyItemScope.(Int, T) -> Unit) {}
@Composable fun LazyColumn(modifier: Modifier = Modifier, verticalArrangement: Any? = null,
    horizontalAlignment: Any? = null, contentPadding: Any? = null, content: LazyListScope.() -> Unit) {}
@Composable fun LazyRow(modifier: Modifier = Modifier, content: LazyListScope.() -> Unit) {}
