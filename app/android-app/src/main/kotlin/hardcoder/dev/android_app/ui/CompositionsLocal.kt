package hardcoder.dev.android_app.ui

import androidx.compose.runtime.compositionLocalOf
import hardcoder.dev.android_app.di.PresentationModule
import hardcoder.dev.android_app.ui.features.waterBalance.DrinkTypeResourcesProvider

val LocalPresentationModule = compositionLocalOf<PresentationModule> {
    error("LocalPresentationModule not provided")
}

val LocalDrinkTypeResourcesProvider = compositionLocalOf<DrinkTypeResourcesProvider> {
    error("LocalDrinkTypeResources not provided")
}

val LocalFloatFormatter = compositionLocalOf<FloatFormatter> {
    error("LocalFloatFormatter not provided")
}

val LocalDateTimeFormatter = compositionLocalOf<DateTimeFormatter> {
    error("LocalDateTimeFormatter not provided")
}