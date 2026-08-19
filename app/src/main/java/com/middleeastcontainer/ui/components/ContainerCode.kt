package com.middleeastcontainer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.middleeastcontainer.ui.theme.BrandGold
import com.middleeastcontainer.ui.theme.StencilFamily

/**
 * The signature treatment of this app: an ISO 6346 code shown the way it is
 * actually stencilled on a container's steel — monospaced, letter-spaced, and
 * split into its three real parts.
 *
 *     CSQU  305438  3
 *     owner  serial  check
 *
 * The check digit is set in signal amber because it is the part that decides
 * whether the code is valid at all.
 */
@Composable
fun ContainerCode(
    code: String,
    modifier: Modifier = Modifier,
    size: ContainerCodeSize = ContainerCodeSize.Medium,
) {
    val owner = code.take(4)
    val serial = code.drop(4).take(6)
    val check = code.drop(10).take(1)

    Row(
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(4.dp),
            )
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
            .padding(horizontal = size.padH, vertical = size.padV),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = owner,
            fontFamily = StencilFamily,
            fontWeight = FontWeight.Bold,
            fontSize = size.font,
            letterSpacing = size.tracking,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = "  $serial",
            fontFamily = StencilFamily,
            fontWeight = FontWeight.Medium,
            fontSize = size.font,
            letterSpacing = size.tracking,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (check.isNotEmpty()) {
            Text(
                text = "  $check",
                fontFamily = StencilFamily,
                fontWeight = FontWeight.Bold,
                fontSize = size.font,
                letterSpacing = size.tracking,
                color = BrandGold,
            )
        }
    }
}

/**
 * Sizes for the stencil plate.
 *
 * A shade larger than an interface label would be, because a container number is
 * not read — it is matched, character by character, against a number painted on
 * steel several metres away. Wide tracking is the point: it separates the owner
 * code from the serial and makes a misread digit visible.
 */
enum class ContainerCodeSize(
    val font: androidx.compose.ui.unit.TextUnit,
    val tracking: androidx.compose.ui.unit.TextUnit,
    val padH: androidx.compose.ui.unit.Dp,
    val padV: androidx.compose.ui.unit.Dp,
) {
    /** In lists, where numbers are checked off against a stack. */
    Small(14.5.sp, 1.4.sp, 9.dp, 5.dp),

    /** Alongside a heading. */
    Medium(17.5.sp, 2.0.sp, 12.dp, 7.dp),

    /** The subject of the screen. */
    Large(23.sp, 2.6.sp, 15.dp, 10.dp),
}

/** Small uppercase label used above sections and beside data. */
@Composable
fun Eyebrow(text: String, modifier: Modifier = Modifier, color: Color? = null) {
    Text(
        text = text.uppercase(),
        modifier = modifier,
        style = MaterialTheme.typography.labelSmall,
        color = color ?: MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
