package hardcoder.dev.androidApp.di.logic.foundation

import hardcoder.dev.database.IdGenerator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val identificationModule = module {
    singleOf(::IdGenerator)
}