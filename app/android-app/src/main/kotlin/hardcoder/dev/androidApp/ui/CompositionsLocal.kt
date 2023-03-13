package hardcoder.dev.androidApp.ui

import androidx.compose.runtime.compositionLocalOf
import hardcoder.dev.androidApp.di.PresentationModule
import hardcoder.dev.androidApp.ui.features.starvation.TimeUnitMapper
import hardcoder.dev.androidApp.ui.features.starvation.plans.StarvationPlanResourcesProvider
import hardcoder.dev.androidApp.ui.features.waterBalance.DrinkTypeResourcesProvider
import hardcoder.dev.androidApp.ui.features.starvation.StarvationStatisticLabelResolver

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

val LocalStarvationPlanResourcesProvider = compositionLocalOf<StarvationPlanResourcesProvider> {
    error("LocalStarvationPlanResourcesProvider not provided")
}

val LocalStarvationStatisticLabelResolver = compositionLocalOf<StarvationStatisticLabelResolver> {
    error("LocalStarvationStatisticLabelResolver not provided")
}

val LocalTimeUnitMapper = compositionLocalOf<TimeUnitMapper> {
    error("LocalStarvationHoursResolver not provided")
}