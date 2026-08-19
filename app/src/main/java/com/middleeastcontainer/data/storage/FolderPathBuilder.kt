package com.middleeastcontainer.data.storage

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Builds the folder layout under /OCR2.
 *
 *     <section>/<yyyy>/<MM>/<yyyy-MM-dd>/<group>/<group> <date> [HH-mm-ss].jpg
 *
 * Dated folders keep a yard's worth of photos navigable by hand — someone
 * looking for last Tuesday's count can find it without the app.
 */
object FolderPathBuilder {

    private val YEAR = SimpleDateFormat("yyyy", Locale.US)
    private val MONTH = SimpleDateFormat("MM", Locale.US)
    private val DAY = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val TIME = SimpleDateFormat("HH-mm-ss", Locale.US)

    /** Strips anything that could break out of the folder or upset a filesystem. */
    /** Exposed so callers can match a folder written earlier. */
    fun safeName(value: String): String = safe(value)

    /**
     * Reduces free text to a single safe path segment.
     *
     * The dot is allowed because real names contain it, which leaves one hole:
     * an input of exactly "." or ".." survives the filter and is a directory
     * reference rather than a name — a zone typed as ".." would write outside
     * the container folder entirely. Zones are typed by inspectors, so this is
     * reachable, not theoretical.
     */
    private fun safe(value: String): String {
        val cleaned = value.trim()
            .replace(Regex("[^A-Za-z0-9._-]"), "_")
            .take(48)
        return when {
            cleaned.isBlank() -> "unknown"
            // "." and ".." navigate; they are never names.
            cleaned.all { it == '.' } -> "unknown"
            else -> cleaned
        }
    }

    fun relativeDir(section: String, group: String, at: Date = Date()): String =
        "${safe(section)}/${YEAR.format(at)}/${MONTH.format(at)}/${DAY.format(at)}/${safe(group)}"

    /**
     * Filename for one capture.
     *
     * No spaces or brackets: they survive most filesystems but complicate USB
     * transfers, shell scripts and the odd Android build, and they buy nothing
     * that an underscore does not.
     */
    fun captureFileName(group: String, at: Date = Date()): String =
        "${safe(group)}_${DAY.format(at)}_${TIME.format(at)}.jpg"

    /** Kept for callers that still pass a Calendar. */
    fun relativeContainerDir(container: String, calendar: Calendar): String =
        relativeDir("Inspection", container, calendar.time)
}
