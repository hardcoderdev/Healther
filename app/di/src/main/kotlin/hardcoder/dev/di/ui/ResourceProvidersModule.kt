package hardcoder.dev.di.ui

import hardcoder.dev.resources.features.diary.DateRangeFilterTypeResourcesProvider
import hardcoder.dev.resources.features.fasting.FastingPlanResourcesProvider
import hardcoder.dev.resources.user.GenderResourcesProvider
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val resourceProvidersModule = module {
    singleOf(::GenderResourcesProvider)
    singleOf(::FastingPlanResourcesProvider)
    singleOf(::DateRangeFilterTypeResourcesProvider)
}