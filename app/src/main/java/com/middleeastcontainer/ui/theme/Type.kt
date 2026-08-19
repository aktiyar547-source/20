package com.middleeastcontainer.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Two voices: a tight sans for interface text, and monospace reserved strictly
 * for container numbers and machine data. Nothing else gets mono — that is what
 * makes a stencilled code read as a code rather than as decoration.
 *
 * Sized for the yard rather than a desk. This is read at arm's length, in direct
 * sun, on a screen that is usually scratched and often cracked, by someone
 * wearing gloves. Every size therefore sits a step above what a comfortable
 * office app would use, and every style carries an explicit line height:
 * Compose otherwise falls back to the font's own metrics, which are tight enough
 * to make a two-line row read as a single block at a glance.
 */
val MecrcTypography = Typography(
    // Screen titles. Negative tracking keeps a long container number compact.
    displaySmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 30.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.5).sp,
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.2).sp,
    ),
    // Row headings — a side name, a zone, a sweep.
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp,
        lineHeight = 24.sp,
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 23.sp,
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 21.sp,
    ),
    // Status lines under a heading: "Not captured", "3 need a closer look".
    // Carries real information, so it stays legible rather than decorative.
    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 13.5.sp,
        lineHeight = 19.sp,
        letterSpacing = 0.1.sp,
    ),
    // Button text. Slightly wider tracking holds up against a coloured fill.
    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.3.sp,
    ),
    // Eyebrows and section markers. Deliberately the smallest thing on screen,
    // because it labels rather than informs — wide tracking carries it.
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 11.5.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.9.sp,
    ),
)

/** Reserved for ISO 6346 codes, device ids and timestamps. */
val StencilFamily = FontFamily.Monospace
