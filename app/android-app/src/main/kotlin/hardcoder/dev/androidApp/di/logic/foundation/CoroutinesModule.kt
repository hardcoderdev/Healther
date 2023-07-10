package hardcoder.dev.androidApp.di.logic.foundation

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.coroutines.DefaultBackgroundBackgroundCoroutineDispatchers
import org.koin.dsl.module

val coroutinesModule = module {
    single<BackgroundCoroutineDispatchers> {
        DefaultBackgroundBackgroundCoroutineDispatchers
    }
}