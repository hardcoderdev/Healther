package hardcoder.dev.di.logic.features

import hardcoder.dev.logics.FeatureTypeProvider
import hardcoder.dev.mappers.features.diary.FeatureTypeIdMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val featuresLogicModule = module {
    singleOf(::FeatureTypeIdMapper)
    singleOf(::FeatureTypeProvider)
}