package hardcoder.dev.di

import hardcoder.dev.di.logic.foundation.coroutinesModule
import hardcoder.dev.di.logic.foundation.dateTimeModule
import org.koin.dsl.module

val foundationModule = module {
    includes(
        coroutinesModule,
        dateTimeModule,
    )
}