package com.middleeastcontainer.data.export

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.util.zip.ZipInputStream

/**
 * The spreadsheet is written by hand rather than with a library, so nothing else
 * verifies that the result is a valid .xlsx. These tests open the bytes as a ZIP
 * and read the parts back — a malformed workbook shows up here rather than as
 * "Excel cannot open the file" on someone's desk.
 */
class XlsxWriterTest {

    private fun write(rows: List<List<XlsxWriter.Cell>>, sheet: String = "Inventory"): ByteArray {
        val out = ByteArrayOutputStream()
        XlsxWriter.write(out, sheet, rows)
        return out.toByteArray()
    }

    private fun parts(bytes: ByteArray): Map<String, String> {
        val found = mutableMapOf<String, String>()
        ZipInputStream(bytes.inputStream()).use { zip ->
            while (true) {
                val entry = zip.nextEntry ?: break
                found[entry.name] = zip.readBytes().toString(Charsets.UTF_8)
            }
        }
        return found
    }

    @Test
    fun `produces the four parts an xlsx reader requires`() {
        val bytes = write(listOf(listOf(XlsxWriter.text("A"))))
        val names = parts(bytes).keys
        assertTrue("[Content_Types].xml" in names)
        assertTrue("_rels/.rels" in names)
        assertTrue("xl/workbook.xml" in names)
        assertTrue("xl/_rels/workbook.xml.rels" in names)
        assertTrue("xl/worksheets/sheet1.xml" in names)
    }

    @Test
    fun `numbers are written as numbers, not text`() {
        val bytes = write(listOf(listOf(XlsxWriter.number(42), XlsxWriter.text("42"))))
        val sheet = parts(bytes).getValue("xl/worksheets/sheet1.xml")
        // A numeric cell carries a bare <v>; a text cell is an inline string.
        assertTrue("numeric cell", sheet.contains("<c r=\"A1\"><v>42</v></c>"))
        assertTrue("text cell", sheet.contains("t=\"inlineStr\""))
    }

    @Test
    fun `columns continue past Z`() {
        val row = (1..28).map { XlsxWriter.text("c$it") }
        val sheet = parts(write(listOf(row))).getValue("xl/worksheets/sheet1.xml")
        assertTrue("Z1 present", sheet.contains("r=\"Z1\""))
        assertTrue("AA1 present", sheet.contains("r=\"AA1\""))
        assertTrue("AB1 present", sheet.contains("r=\"AB1\""))
    }

    @Test
    fun `characters that would break the XML are escaped`() {
        val bytes = write(listOf(listOf(XlsxWriter.text("Row C & D <\"test\">"))))
        val sheet = parts(bytes).getValue("xl/worksheets/sheet1.xml")
        assertTrue(sheet.contains("&amp;"))
        assertTrue(sheet.contains("&lt;"))
        assertTrue(sheet.contains("&gt;"))
        // A raw ampersand would make the file unopenable.
        assertTrue("no unescaped &", !Regex("&(?!amp;|lt;|gt;|quot;|apos;)").containsMatchIn(sheet))
    }

    @Test
    fun `control characters are stripped rather than corrupting the file`() {
        // OCR occasionally emits these; Excel reports the result as corruption.
        val bytes = write(listOf(listOf(XlsxWriter.text("AB\u0001C\u0007D"))))
        val sheet = parts(bytes).getValue("xl/worksheets/sheet1.xml")
        assertTrue(sheet.contains("<t>ABCD</t>"))
    }

    @Test
    fun `sheet name is carried into the workbook`() {
        val book = parts(write(listOf(listOf(XlsxWriter.text("x"))), "Row C")).getValue("xl/workbook.xml")
        assertTrue(book.contains("name=\"Row C\""))
    }

    @Test
    fun `every row is numbered from one`() {
        val rows = (1..5).map { listOf(XlsxWriter.text("r$it")) }
        val sheet = parts(write(rows)).getValue("xl/worksheets/sheet1.xml")
        for (n in 1..5) {
            assertTrue("row $n", sheet.contains("<row r=\"$n\">"))
        }
    }

    @Test
    fun `an empty sheet is still a readable workbook`() {
        val names = parts(write(emptyList())).keys
        assertEquals(5, names.size)
    }
}
