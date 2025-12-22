package kr.ac.kumoh.s20230625.tint_song2.util

import androidx.compose.ui.graphics.Color

val tintColorHexMap: Map<String, String> = mapOf(
    "레드" to "#FF0000",
    "핑크" to "#FF69B4",
    "코랄" to "#FF7F50",
    "MLBB" to "#B38080",
    "브라운" to "#A52A2A",
    "오렌지" to "#FFA500",
    "로즈" to "#CC6680",
    "누드" to "#FCC7C7",
    "모브" to "#DB94C7",
)

val tintColors: Map<String, Color> =
    tintColorHexMap.mapValues { ColorHex.fromHex(it.value) }