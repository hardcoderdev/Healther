package hardcoder.dev.androidApp.ui

import androidx.compose.runtime.compositionLocalOf
import hardcoder.dev.androidApp.di.PresentationModule
import hardcoder.dev.androidApp.ui.features.fasting.plans.FastingPlanResourcesProvider
import hardcoder.dev.androidApp.ui.setUpFlow.gender.GenderResourcesProvider
import hardcoder.dev.datetime.TimeUnitMapper

val LocalPresentationModule = compositionLocalOf<PresentationModule> {
    error("LocalPresentationModule not provided")
}

val LocalIconResolver = compositionLocalOf<IconResolver> {
    error("LocalIconResolver not provided")
}

val LocalFloatFormatter = compositionLocalOf<FloatFormatter> {
    error("LocalFloatFormatter not provided")
}

val LocalDateTimeFormatter = compositionLocalOf<DateTimeFormatter> {
    error("LocalDateTimeFormatter not provided")
}

val LocalGenderResourcesProvider = compositionLocalOf<GenderResourcesProvider> {
    error("LocalGenderResourcesProvider not provided")
}

val LocalFastingPlanResourcesProvider = compositionLocalOf<FastingPlanResourcesProvider> {
    error("LocalFastingPlanResourcesProvider not provided")
}

val LocalTimeUnitMapper = compositionLocalOf<TimeUnitMapper> {
    error("LocalTimeUnitMapper not provided")
}