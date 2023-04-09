package hardcoder.dev.androidApp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import hardcoder.dev.androidApp.ui.dashboard.DashboardScreen
import hardcoder.dev.androidApp.ui.splash.SplashScreen

@Composable
fun RootScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Root.route) {
        composable(route = Screen.Root.route) {
            SplashScreen(
                onStartSetUp = { navController.navigate(Screen.Welcome.route) },
                onNavigateToDashboard = { navController.navigate(Screen.Diary.route) }
            )
        }
        navigation(
            startDestination = Flow.SetUpFlow.startDestination,
            route = Flow.SetUpFlow.route
        ) {
            setUpDestinations(navController)
        }

        navigation(
            startDestination = Flow.DiaryFlow.startDestination,
            route = Flow.DiaryFlow.route
        ) {
            diaryDestinations(navController)
        }

        composable(route = Screen.Dashboard.route) {
            DashboardScreen()
        }

        navigation(
            startDestination = Flow.WaterTrackingFlow.startDestination,
            route = Flow.WaterTrackingFlow.route
        ) {
            waterTrackingDestinations(navController)
        }

        navigation(
            startDestination = Flow.PedometerFlow.startDestination,
            route = Flow.PedometerFlow.route
        ) {
            pedometerDestinations(navController)
        }

        navigation(
            startDestination = Flow.FastingFlow.startDestination,
            route = Flow.FastingFlow.route
        ) {
            fastingDestinations(navController)
        }

        navigation(
            startDestination = Flow.MoodTrackingFlow.startDestination,
            route = Flow.MoodTrackingFlow.route
        ) {
            moodTrackingDestinations(navController)
        }
    }
}





