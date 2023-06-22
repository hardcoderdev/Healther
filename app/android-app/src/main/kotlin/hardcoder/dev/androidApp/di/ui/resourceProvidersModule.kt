package hardcoder.dev.androidApp.di.ui

import hardcoder.dev.androidApp.ui.features.diary.DateRangeFilterTypeResourcesProvider
import hardcoder.dev.androidApp.ui.features.fasting.plans.FastingPlanResourcesProvider
import hardcoder.dev.androidApp.ui.setUpFlow.gender.GenderResourcesProvider
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val resourceProvidersModule = module {
    singleOf(::GenderResourcesProvider)
    singleOf(::FastingPlanResourcesProvider)
    singleOf(::DateRangeFilterTypeResourcesProvider)

}