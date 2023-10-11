package hardcoder.dev.di.logic.foundation

import hardcoder.dev.identification.IdGenerator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal val identificationModule = module {
    single {
        IdGenerator(
            context = androidContext(),
        )
    }
}