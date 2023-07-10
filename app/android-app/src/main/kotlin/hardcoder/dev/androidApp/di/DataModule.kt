package hardcoder.dev.androidApp.di

import hardcoder.dev.androidApp.di.data.databaseModule
import org.koin.dsl.module

val dataModule = module {
    includes(
        databaseModule,
    )
}