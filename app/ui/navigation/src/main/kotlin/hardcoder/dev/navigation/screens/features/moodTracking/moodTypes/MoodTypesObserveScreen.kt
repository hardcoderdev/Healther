package hardcoder.dev.navigation.screens.features.moodTracking.moodTypes

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypesViewModel
import hardcoder.dev.screens.features.moodTracking.moodType.MoodTypes
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun MoodTypesObserveScreen(navController: NavController) {
    val viewModel = koinViewModel<MoodTypesViewModel>()

    MoodTypes(
        moodTypesLoadingController = viewModel.moodTypesLoadingController,
        onGoBack = navController::popBackStack,
        onCreateMoodType = {
            navController.navigate(Screen.MoodTypesCreate.route)
        },
        onUpdateMoodType = { moodTypeId ->
            navController.navigate(Screen.MoodTypesUpdate.buildRoute(moodTypeId))
        },
    )
}