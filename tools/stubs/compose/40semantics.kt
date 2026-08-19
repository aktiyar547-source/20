package androidx.compose.ui.semantics
import androidx.compose.ui.Modifier
class SemanticsPropertyReceiver
// Extension property in the real API, not a member.
var SemanticsPropertyReceiver.contentDescription: String
    get() = ""
    set(value) {}
fun Modifier.semantics(mergeDescendants: Boolean = false,
    properties: SemanticsPropertyReceiver.() -> Unit): Modifier = this
fun Modifier.clearAndSetSemantics(properties: SemanticsPropertyReceiver.() -> Unit): Modifier = this
