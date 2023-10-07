package hardcoder.dev.di

import hardcoder.dev.di.ui.formattersModule
import hardcoder.dev.di.ui.resolversModule
import hardcoder.dev.di.ui.resourceMappersModule
import hardcoder.dev.di.ui.resourceProvidersModule
import org.koin.dsl.module

val uiModule = module {
    includes(
        formattersModule,
        resourceProvidersModule,
        resourceMappersModule,
        resolversModule,
    )
}