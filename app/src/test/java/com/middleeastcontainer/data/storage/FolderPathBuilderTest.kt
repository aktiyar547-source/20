package com.middleeastcontainer.data.storage

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Zone and container names reach this straight from a text field, so it is the
 * only thing standing between typed input and the filesystem.
 *
 * The dot-only cases are not theoretical: "." and ".." pass a character filter
 * that allows dots, and as a path segment they navigate rather than name. A zone
 * typed as ".." once wrote outside the container folder entirely.
 */
class FolderPathBuilderTest {

    private val at = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        .parse("2026-08-19 01:04:29")!!

    @Test
    fun `builds a dated folder layout`() {
        assertEquals(
            "Inspection/2026/08/2026-08-19/BSIU2888813",
            FolderPathBuilder.relativeDir("Inspection", "BSIU2888813", at),
        )
    }

    @Test
    fun `filenames avoid spaces and brackets`() {
        val name = FolderPathBuilder.captureFileName("BSIU2888813", at)
        assertTrue(name, !name.contains(" "))
        assertTrue(name, !name.contains("[") && !name.contains("]"))
        assertTrue(name, name.endsWith(".jpg"))
    }

    @Test
    fun `a dot-only name cannot escape the folder`() {
        for (input in listOf("..", ".", "...", " .. ")) {
            val segment = FolderPathBuilder.relativeDir("Inventory", input, at)
                .split("/").last()
            assertTrue("'$input' produced '$segment'", segment !in setOf(".", ".."))
        }
    }

    @Test
    fun `separators inside a name are neutralised`() {
        val dir = FolderPathBuilder.relativeDir("Inventory", "../../etc/passwd", at)
        assertEquals("stays one segment", 5, dir.split("/").size)
    }

    @Test
    fun `blank names get a placeholder`() {
        assertTrue(
            FolderPathBuilder.relativeDir("Inventory", "   ", at).endsWith("/unknown")
        )
    }

    @Test
    fun `long names are truncated`() {
        val segment = FolderPathBuilder.relativeDir("Inventory", "X".repeat(200), at)
            .split("/").last()
        assertTrue(segment.length.toString(), segment.length <= 48)
    }

    @Test
    fun `unsafe characters are replaced`() {
        val name = FolderPathBuilder.captureFileName("Row Ç#1", at)
        assertTrue(name, Regex("^[A-Za-z0-9._-]+\\.jpg$").matches(name))
    }
}
