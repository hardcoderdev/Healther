package hardcoder.dev.di.logic.foundation

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.coroutines.DefaultBackgroundBackgroundCoroutineDispatchers
import org.koin.dsl.module

internal val coroutinesModule = module {
    single<BackgroundCoroutineDispatchers> {
        DefaultBackgroundBackgroundCoroutineDispatchers
    }
}