package org.junit

/**
 * Minimal JUnit stand-ins, so test sources can be type-checked here.
 *
 * The real jar is not obtainable in this environment, and it is not needed: the
 * point is to compile the tests, which is where a fake that no longer implements
 * its interface shows up. That exact failure reached CI once because every
 * checker scanned main sources only.
 */
annotation class Test
annotation class Before
annotation class After
annotation class Ignore(val value: String = "")

object Assert {
    fun assertEquals(expected: Any?, actual: Any?) {}
    fun assertEquals(message: String, expected: Any?, actual: Any?) {}
    fun assertEquals(expected: Int, actual: Int) {}
    fun assertEquals(message: String, expected: Int, actual: Int) {}
    fun assertEquals(expected: Long, actual: Long) {}
    fun assertEquals(expected: Double, actual: Double, delta: Double) {}
    fun assertTrue(condition: Boolean) {}
    fun assertTrue(message: String, condition: Boolean) {}
    fun assertFalse(condition: Boolean) {}
    fun assertFalse(message: String, condition: Boolean) {}
    fun assertNull(actual: Any?) {}
    fun assertNull(message: String, actual: Any?) {}
    fun assertNotNull(actual: Any?) {}
    fun assertNotNull(message: String, actual: Any?) {}
    fun assertSame(expected: Any?, actual: Any?) {}
    fun assertArrayEquals(expected: Any?, actual: Any?) {}
    fun fail(message: String = "") {}
}
