package hardcoder.dev.androidApp.di.logic

import hardcoder.dev.logic.appPreferences.PredefinedTracksManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val predefinedLogicModule = module {
    single {
        PredefinedTracksManager(
            context = androidContext(),
            drinkTypeCreator = get(),
            moodTypeCreator = get()
        )
    }
}