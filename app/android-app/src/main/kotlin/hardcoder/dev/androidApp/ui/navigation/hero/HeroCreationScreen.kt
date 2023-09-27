package hardcoder.dev.androidApp.ui.navigation.hero

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.navigation.dashboard.DashboardScreen
import hardcoder.dev.androidApp.ui.screens.hero.GenderResourcesProvider
import hardcoder.dev.androidApp.ui.screens.hero.creation.HeroCreation
import hardcoder.dev.presentation.hero.HeroCreationViewModel
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

class HeroCreationScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<HeroCreationViewModel>()
        val genderResourcesProvider = koinInject<GenderResourcesProvider>()

        LaunchedEffectWhenExecuted(
            controller = viewModel.heroCreationController,
            action = {
                navigator += DashboardScreen()
            },
        )

        HeroCreation(
            genderResourcesProvider = genderResourcesProvider,
            heroCreationController = viewModel.heroCreationController,
            genderSelectionController = viewModel.genderSelectionController,
            nameInputController = viewModel.nameInputController,
            weightInputController = viewModel.weightInputController,
            exerciseStressTimeInputController = viewModel.exerciseStressTimeInputController,
        )
    }
}