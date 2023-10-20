package hardcoder.dev.di.logic

import hardcoder.dev.logics.appPreferences.AppPreferenceProvider
import hardcoder.dev.logics.appPreferences.AppPreferenceUpdater
import org.koin.dsl.module

internal val appPreferencesLogicModule = module {

    single {
        AppPreferenceUpdater(
            appPreferencesDao = get(),
            dispatchers = get(),
        )
    }

    single {
        AppPreferenceProvider(
            appPreferencesDao = get(),
            dispatchers = get(),
        )
    }
}