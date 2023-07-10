package hardcoder.dev.androidApp.ui.navigation.hero

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.navigation.dashboard.DashboardScreen
import hardcoder.dev.androidApp.ui.screens.hero.HeroCreation
import hardcoder.dev.presentation.hero.HeroCreationViewModel
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel

class HeroCreationScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<HeroCreationViewModel>()

        LaunchedEffectWhenExecuted(
            controller = viewModel.heroCreationController,
            action = {
                navigator += DashboardScreen()
            },
        )

        HeroCreation(
            viewModel = viewModel,
            onGoBack = navigator::pop,
        )
    }
}