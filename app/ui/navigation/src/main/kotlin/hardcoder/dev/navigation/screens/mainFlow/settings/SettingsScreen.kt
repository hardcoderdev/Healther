package hardcoder.dev.navigation.screens.mainFlow.settings

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.presentation.settings.SettingsViewModel
import hardcoder.dev.screens.settings.Settings
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SettingsScreen(navController: NavController) {
    val viewModel = koinViewModel<SettingsViewModel>()

    Settings(
        preferencesLoadingController = viewModel.preferencesLoadingController,
        onGoBack = navController::popBackStack,
    )
}