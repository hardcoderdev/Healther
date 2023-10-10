package hardcoder.dev.di.logic.foundation

import hardcoder.dev.identification.IdGenerator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val identificationModule = module {
    singleOf(::IdGenerator)
}