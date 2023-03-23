package hardcoder.dev.androidApp.ui

import androidx.compose.runtime.compositionLocalOf
import hardcoder.dev.androidApp.di.PresentationModule
import hardcoder.dev.androidApp.ui.features.starvation.plans.StarvationPlanResourcesProvider
import hardcoder.dev.datetime.TimeUnitMapper

val LocalPresentationModule = compositionLocalOf<PresentationModule> {
    error("LocalPresentationModule not provided")
}

val LocalIconProvider = compositionLocalOf<IconProvider> {
    error("LocalIconProvider not provided")
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

val LocalStarvationPlanResourcesProvider = compositionLocalOf<StarvationPlanResourcesProvider> {
    error("LocalStarvationPlanResourcesProvider not provided")
}

val LocalTimeUnitMapper = compositionLocalOf<TimeUnitMapper> {
    error("LocalStarvationHoursResolver not provided")
}