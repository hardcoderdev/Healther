package hardcoder.dev.di.logic

import hardcoder.dev.logics.appPreferences.AppPreferenceProvider
import hardcoder.dev.logics.appPreferences.AppPreferenceUpdater
import org.koin.dsl.module

internal val appPreferencesLogicModule = module {

    single {
        AppPreferenceUpdater(
            appDatabase = get(),
            dispatchers = get(),
        )
    }

    single {
        AppPreferenceProvider(
            appDatabase = get(),
            dispatchers = get(),
        )
    }
}