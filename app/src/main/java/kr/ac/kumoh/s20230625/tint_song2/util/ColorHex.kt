package kr.ac.kumoh.s20230625.tint_song2.util

import androidx.compose.ui.graphics.Color
import kotlin.math.roundToInt

object ColorHex {
    fun toHex(color: Color): String {
        val r = (color.red * 255).roundToInt().coerceIn(0, 255)
        val g = (color.green * 255).roundToInt().coerceIn(0, 255)
        val b = (color.blue * 255).roundToInt().coerceIn(0, 255)
        return String.format("#%02X%02X%02X", r, g, b)
    }

    fun fromHex(hex: String): Color {
        val clean = hex.trim().removePrefix("#")
        val v = clean.toLong(16)
        val r = ((v shr 16) and 0xFF).toInt()
        val g = ((v shr 8) and 0xFF).toInt()
        val b = (v and 0xFF).toInt()
        return Color(r / 255f, g / 255f, b / 255f, 1f)
    }
}