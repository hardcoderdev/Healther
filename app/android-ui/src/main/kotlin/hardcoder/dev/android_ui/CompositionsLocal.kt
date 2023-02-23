package hardcoder.dev.android_ui

import androidx.compose.runtime.compositionLocalOf
import hardcoder.dev.android_ui.waterBalance.DrinkTypeResourcesProvider
import hardcoder.dev.di.PresentationModule

val LocalPresentationModule = compositionLocalOf<PresentationModule> {
    error("LocalPresentationModule not provided")
}

val LocalDrinkTypeResourcesProvider = compositionLocalOf<DrinkTypeResourcesProvider> {
    error("LocalDrinkTypeResources not provided")
}