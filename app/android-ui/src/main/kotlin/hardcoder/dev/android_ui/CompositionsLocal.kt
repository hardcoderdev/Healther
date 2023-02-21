package hardcoder.dev.android_ui

import androidx.compose.runtime.compositionLocalOf
import hardcoder.dev.di.PresentationModule

val LocalPresentationModule = compositionLocalOf<PresentationModule> {
    error("LocalPresentationModule not provided")
}

val LocalDrinkTypeResources = compositionLocalOf<DrinkTypeResourcesProvider> {
    error("LocalDrinkTypeResources not provided")
}