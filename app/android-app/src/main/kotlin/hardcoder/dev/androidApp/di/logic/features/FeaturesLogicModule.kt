package hardcoder.dev.androidApp.di.logic.features

import hardcoder.dev.logic.FeatureTypeProvider
import hardcoder.dev.logic.features.FeatureTypeIdMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val featuresLogicModule = module {
    singleOf(::FeatureTypeIdMapper)
    singleOf(::FeatureTypeProvider)
}