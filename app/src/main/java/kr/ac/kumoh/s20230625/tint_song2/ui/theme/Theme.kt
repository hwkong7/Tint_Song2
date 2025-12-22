package kr.ac.kumoh.s20230625.tint_song2.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = IOSBlue,
    background = IOSDarkBg,
    surface = IOSDarkSurface,
    onPrimary = Color.White,
    onBackground = IOSDarkText,
    onSurface = IOSDarkText,

    secondary = IOSDarkSecondary,
    onSecondary = IOSDarkBg,
)

private val LightColorScheme = lightColorScheme(
    primary = IOSBlue,

    // 전체 배경(Scaffold 배경)
    background = IOSGroupedBg,
    onBackground = IOSTextPrimary,

    // 카드/시트/리스트 아이템 배경
    surface = IOSSurface,
    onSurface = IOSTextPrimary,

    // 구분선/보더 느낌에 가까운 값들
    outline = IOSDivider,
    outlineVariant = IOSDivider,

    secondary = IOSTextSecondary,
    onSecondary = IOSTextPrimary,

    // AppBar도 흰색으로
    surfaceVariant = IOSSurface,
    onSurfaceVariant = IOSTextPrimary,
)

@Composable
fun Tint_SongTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // ✅ iOS 느낌 유지하려면 dynamicColor 끄는 게 좋아!
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
