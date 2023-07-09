package hardcoder.dev.androidApp.di.logic

import hardcoder.dev.logic.appPreferences.AppPreferenceProvider
import hardcoder.dev.logic.appPreferences.AppPreferenceUpdater
import org.koin.dsl.module

val appPreferencesLogicModule = module {

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