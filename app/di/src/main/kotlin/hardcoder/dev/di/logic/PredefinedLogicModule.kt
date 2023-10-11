package hardcoder.dev.di.logic

import hardcoder.dev.logics.appPreferences.PredefinedTracksManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal val predefinedLogicModule = module {
    single {
        PredefinedTracksManager(
            context = androidContext(),
            drinkTypeCreator = get(),
            moodTypeCreator = get(),
        )
    }
}