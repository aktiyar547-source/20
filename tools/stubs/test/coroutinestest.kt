package kotlinx.coroutines.test

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.EmptyCoroutineContext

/** Stand-in for kotlinx-coroutines-test, which is not obtainable here. */
class TestScope : CoroutineScope {
    override val coroutineContext = EmptyCoroutineContext
}

fun runTest(block: suspend TestScope.() -> Unit) {}
