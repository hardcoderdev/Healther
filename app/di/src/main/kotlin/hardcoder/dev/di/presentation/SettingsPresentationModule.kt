package hardcoder.dev.di.presentation

import hardcoder.dev.presentation.settings.SettingsViewModel
import org.koin.dsl.module

internal val settingsPresentationModule = module {
    factory {
        SettingsViewModel(
            appPreferenceProvider = get(),
        )
    }
}