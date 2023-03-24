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
                onNavigateToDashboard = { navController.navigate(Screen.PedometerFeature.route) }
            )
        }
        navigation(
            startDestination = Flow.SetUpFlow.startDestination,
            route = Flow.SetUpFlow.route
        ) {
            addSetUpDestinations(navController)
        }

        composable(route = Screen.Dashboard.route) {
            DashboardScreen()
        }

        navigation(
            startDestination = Flow.WaterTrackingFlow.startDestination,
            route = Flow.WaterTrackingFlow.route
        ) {
            addWaterTrackingDestinations(navController)
        }

        navigation(
            startDestination = Flow.PedometerFlow.startDestination,
            route = Flow.PedometerFlow.route
        ) {
            addPedometerDestinations(navController)
        }

        navigation(
            startDestination = Flow.StarvationFlow.startDestination,
            route = Flow.StarvationFlow.route
        ) {
            addStarvationDestinations(navController)
        }
    }
}





