package hardcoder.dev.di.presentation

import hardcoder.dev.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val settingsPresentationModule = module {
    single {
        SettingsViewModel(
            appPreferenceProvider = get(),
        )
    }
}