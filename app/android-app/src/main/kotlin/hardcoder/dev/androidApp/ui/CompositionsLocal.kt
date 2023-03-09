package hardcoder.dev.androidApp.ui

import androidx.compose.runtime.compositionLocalOf
import hardcoder.dev.androidApp.di.PresentationModule
import hardcoder.dev.androidApp.ui.features.waterBalance.DrinkTypeResourcesProvider

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