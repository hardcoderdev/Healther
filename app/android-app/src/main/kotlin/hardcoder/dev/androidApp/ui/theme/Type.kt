package hardcoder.dev.androidApp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import hardcoder.dev.healther.R

val rubikFontFamily = FontFamily(
    Font(
        resId = R.font.rubik_bold,
        weight = FontWeight.Bold,
        style = FontStyle.Normal
    ),
    Font(
        resId = R.font.rubik_italic,
        weight = FontWeight.Normal,
        style = FontStyle.Italic
    ),
    Font(
        resId = R.font.rubik_medium,
        weight = FontWeight.Medium,
        style = FontStyle.Normal
    ),
    Font(
        resId = R.font.rubik_regular,
        weight = FontWeight.Normal,
        style = FontStyle.Normal
    )
)

val defaultTypography = Typography()
val Typography = Typography(
    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = rubikFontFamily),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = rubikFontFamily),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = rubikFontFamily),
    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = rubikFontFamily),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = rubikFontFamily),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = rubikFontFamily),
    titleLarge = defaultTypography.titleLarge.copy(fontFamily = rubikFontFamily),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = rubikFontFamily),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = rubikFontFamily),
    labelLarge = defaultTypography.labelLarge.copy(fontFamily = rubikFontFamily),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = rubikFontFamily),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = rubikFontFamily)
)