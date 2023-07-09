package hardcoder.dev.androidApp.di

import hardcoder.dev.androidApp.di.ui.formattersModule
import hardcoder.dev.androidApp.di.ui.resolversModule
import hardcoder.dev.androidApp.di.ui.resourceMappersModule
import hardcoder.dev.androidApp.di.ui.resourceProvidersModule
import org.koin.dsl.module

val uiModule = module {
    includes(
        formattersModule,
        resourceProvidersModule,
        resourceMappersModule,
        resolversModule,
    )
}