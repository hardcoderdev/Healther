package hardcoder.dev.androidApp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import hardcoder.dev.androidApp.ui.dashboard.DashboardScreen
import hardcoder.dev.androidApp.ui.settings.SettingsScreen
import hardcoder.dev.androidApp.ui.splash.SplashScreen

@Composable
fun RootScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Root.route) {
        composable(route = Screen.Root.route) {
            SplashScreen(
                onStartSetUp = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Root.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Root.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        navigation(
            startDestination = NavGraph.SetUpNavGraph.startDestination,
            route = NavGraph.SetUpNavGraph.route
        ) {
            setUpDestinations(navController)
        }

        navigation(
            startDestination = NavGraph.DiaryNavGraph.startDestination,
            route = NavGraph.DiaryNavGraph.route
        ) {
            diaryDestinations(navController)
        }

        composable(route = Screen.Dashboard.route) {
            DashboardScreen(
                onGoToWaterTrackingFeature = {
                    navController.navigate(Screen.WaterTrackingFeature.route)
                },
                onGoToPedometerFeature = {
                    navController.navigate(Screen.PedometerFeature.route)
                },
                onGoToFastingFeature = {
                    navController.navigate(Screen.FastingFeature.route)
                },
                onStartFasting = {
                    navController.navigate(Screen.FastingCreateTrack.route)
                },
                onGoToMoodTrackingFeature = {
                    navController.navigate(Screen.MoodTrackingFeature.route)
                },
                onGoToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(route = Screen.Settings.route) {
            SettingsScreen(onGoBack = navController::popBackStack)
        }

        navigation(
            startDestination = NavGraph.WaterTrackingNavGraph.startDestination,
            route = NavGraph.WaterTrackingNavGraph.route
        ) {
            waterTrackingDestinations(navController)
        }

        navigation(
            startDestination = NavGraph.PedometerNavGraph.startDestination,
            route = NavGraph.PedometerNavGraph.route
        ) {
            pedometerDestinations(navController)
        }

        navigation(
            startDestination = NavGraph.FastingNavGraph.startDestination,
            route = NavGraph.FastingNavGraph.route
        ) {
            fastingDestinations(navController)
        }

        navigation(
            startDestination = NavGraph.MoodTrackingNavGraph.startDestination,
            route = NavGraph.MoodTrackingNavGraph.route
        ) {
            moodTrackingDestinations(navController)
        }
    }
}





