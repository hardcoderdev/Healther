package hardcoder.dev.androidApp.ui.navigation.settings

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.screens.settings.Settings
import hardcoder.dev.presentation.settings.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

class SettingsScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<SettingsViewModel>()

        Settings(
            preferencesLoadingController = viewModel.preferencesLoadingController,
            appReviewRequestController = viewModel.appReviewRequestController,
            onGoBack = navigator::pop,
        )
    }
}