package hardcoder.dev.navigation.screens.features.moodTracking.moodTypes

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypeCreationViewModel
import hardcoder.dev.screens.features.moodTracking.moodType.create.MoodTypeCreation
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun MoodTypesCreateScreen(navController: NavController) {
    val viewModel = koinViewModel<MoodTypeCreationViewModel>()

    LaunchedEffectWhenExecuted(
        controller = viewModel.creationController,
        action = navController::popBackStack,
    )

    MoodTypeCreation(
        moodTypeNameController = viewModel.moodTypeNameController,
        iconSelectionController = viewModel.iconSelectionController,
        positiveIndexController = viewModel.positiveIndexController,
        creationController = viewModel.creationController,
        onGoBack = navController::popBackStack,
    )
}