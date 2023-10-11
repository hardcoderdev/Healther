package hardcoder.dev.navigation.settings

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.presentation.settings.SettingsViewModel
import hardcoder.dev.screens.settings.Settings

class SettingsScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<SettingsViewModel>()

        Settings(
            preferencesLoadingController = viewModel.preferencesLoadingController,
            onGoBack = navigator::pop,
        )
    }
}