package hardcoder.dev.androidApp.di.presentation

import hardcoder.dev.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsPresentationModule = module {
    viewModel {
        SettingsViewModel(
            reviewManager = get(),
            appPreferenceProvider = get(),
            appPreferenceUpdater = get(),
        )
    }
}