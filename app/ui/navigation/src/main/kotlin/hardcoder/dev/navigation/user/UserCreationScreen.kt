package hardcoder.dev.navigation.user

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.navigation.dashboard.DashboardScreen
import hardcoder.dev.presentation.user.UserCreationViewModel
import hardcoder.dev.resources.user.GenderResourcesProvider
import hardcoder.dev.screens.user.creation.UserCreation
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.compose.koinInject

class UserCreationScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<UserCreationViewModel>()
        val genderResourcesProvider = koinInject<GenderResourcesProvider>()

        LaunchedEffectWhenExecuted(
            controller = viewModel.userCreationController,
            action = {
                navigator += DashboardScreen()
            },
        )

        UserCreation(
            genderResourcesProvider = genderResourcesProvider,
            userCreationController = viewModel.userCreationController,
            genderSelectionController = viewModel.genderSelectionController,
            nameInputController = viewModel.nameInputController,
            weightInputController = viewModel.weightInputController,
            exerciseStressTimeInputController = viewModel.exerciseStressTimeInputController,
        )
    }
}