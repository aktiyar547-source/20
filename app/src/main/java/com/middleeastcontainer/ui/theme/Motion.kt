package com.middleeastcontainer.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

/**
 * One motion vocabulary for the whole app.
 *
 * Defined centrally because inconsistent timing is what makes an interface feel
 * amateur — the same gesture answering in 120 ms on one screen and 400 ms on the
 * next reads as unreliability, even when nobody can say why.
 *
 * Durations are deliberately short. This is used at arm's length, outdoors,
 * while working; animation here exists to explain what moved where, not to be
 * admired. Anything above about a quarter of a second starts to feel like the
 * app is thinking rather than responding.
 */
object Motion {

    /**
     * Decelerating: fast to start, easing into place.
     *
     * For things arriving on screen, which should feel like they were already on
     * their way rather than starting from rest.
     */
    val Enter: Easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)

    /** Accelerating, for things leaving — they need no ceremony. */
    val Exit: Easing = CubicBezierEasing(0.3f, 0.0f, 0.8f, 0.15f)

    /** Symmetrical, for a value changing in place. */
    val Standard: Easing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)

    const val QUICK = 140      // press feedback, small state flips
    const val MEDIUM = 220     // panels and strips arriving
    const val SCREEN = 260     // whole-screen transitions

    fun <T> quick(): FiniteAnimationSpec<T> = tween(QUICK, easing = Standard)
    fun <T> enter(): FiniteAnimationSpec<T> = tween(MEDIUM, easing = Enter)
    fun <T> exit(): FiniteAnimationSpec<T> = tween(QUICK, easing = Exit)

    /**
     * A little overshoot, used only where something should feel physical —
     * the shutter, a count ticking up. Never on layout, where bouncing reads
     * as instability.
     */
    fun <T> springy(): FiniteAnimationSpec<T> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium,
    )
}
