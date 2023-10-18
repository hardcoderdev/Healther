package hardcoder.dev.navigation.screens.mainFlow.user

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.presentation.user.UserCreationViewModel
import hardcoder.dev.resources.user.GenderResourcesProvider
import hardcoder.dev.screens.user.creation.UserCreation
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
internal fun UserCreationScreen(navController: NavController) {
    val viewModel = koinViewModel<UserCreationViewModel>()
    val genderResourcesProvider = koinInject<GenderResourcesProvider>()

    LaunchedEffectWhenExecuted(
        controller = viewModel.userCreationController,
        action = {
            navController.navigate(Screen.Dashboard.route)
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