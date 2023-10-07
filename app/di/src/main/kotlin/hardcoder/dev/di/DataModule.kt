package hardcoder.dev.di

import hardcoder.dev.di.data.databaseModule
import org.koin.dsl.module

val dataModule = module {
    includes(
        databaseModule,
    )
}