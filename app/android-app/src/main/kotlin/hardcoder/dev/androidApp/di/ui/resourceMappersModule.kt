package hardcoder.dev.androidApp.di.ui

import hardcoder.dev.androidApp.ui.features.pedometer.PedometerRejectedMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val resourceMappersModule = module {
    singleOf(::PedometerRejectedMapper)
}