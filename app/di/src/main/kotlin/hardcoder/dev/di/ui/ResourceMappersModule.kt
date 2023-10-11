package hardcoder.dev.di.ui

import hardcoder.dev.pedometer_manager.permissions.PedometerRejectedMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val resourceMappersModule = module {
    singleOf(::PedometerRejectedMapper)
}