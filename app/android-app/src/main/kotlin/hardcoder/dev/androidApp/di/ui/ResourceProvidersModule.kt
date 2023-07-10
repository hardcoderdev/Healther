package hardcoder.dev.androidApp.di.ui

import hardcoder.dev.androidApp.ui.screens.features.diary.DateRangeFilterTypeResourcesProvider
import hardcoder.dev.androidApp.ui.screens.features.fasting.plans.FastingPlanResourcesProvider
import hardcoder.dev.androidApp.ui.screens.hero.GenderResourcesProvider
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val resourceProvidersModule = module {
    singleOf(::GenderResourcesProvider)
    singleOf(::FastingPlanResourcesProvider)
    singleOf(::DateRangeFilterTypeResourcesProvider)
}