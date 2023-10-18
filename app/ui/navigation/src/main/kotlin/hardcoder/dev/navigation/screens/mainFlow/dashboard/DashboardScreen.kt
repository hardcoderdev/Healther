package hardcoder.dev.navigation.screens.mainFlow.dashboard

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.presentation.dashboard.DashboardViewModel
import hardcoder.dev.screens.dashboard.Dashboard
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun DashboardScreen(navController: NavController) {
    val viewModel = koinViewModel<DashboardViewModel>()

    Dashboard(
        featureItemsLoadingController = viewModel.featuresLoadingController,
        pedometerToggleController = viewModel.pedometerToggleController,
        onGoToWaterTrackingFeature = {
            navController.navigate(Screen.WaterTracksObserve.route)
        },
        onCreateWaterTrack = {
            navController.navigate(Screen.WaterTracksCreate.route)
        },
        onGoToFoodTrackingFeature = {
            navController.navigate(Screen.FoodTracksObserve.route)
        },
        onCreateFoodTrack = {
            navController.navigate(Screen.FoodTracksCreate.route)
        },
        onGoToMoodTrackingFeature = {
            navController.navigate(Screen.MoodTracksObserve.route)
        },
        onCreateMoodTrack = {
            navController.navigate(Screen.MoodTracksCreate.route)
        },
        onGoToPedometerFeature = {
            navController.navigate(Screen.PedometerTracksObserve.route)
        },
        onGoToDiary = {
            navController.navigate(Screen.DiaryTracksObserve.route)
        },
        onCreateDiaryTrack = {
            navController.navigate(Screen.DiaryTracksCreate.route)
        },
        onGoToSettings = {
            navController.navigate(Screen.Settings.route)
        },
    )
}