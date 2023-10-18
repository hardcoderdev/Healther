package hardcoder.dev.navigation.graphs.mainFlow

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import hardcoder.dev.navigation.routes.NavGraph
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.navigation.screens.mainFlow.dashboard.DashboardScreen
import hardcoder.dev.navigation.screens.mainFlow.settings.SettingsScreen
import hardcoder.dev.navigation.screens.mainFlow.splash.SplashScreen
import hardcoder.dev.navigation.screens.mainFlow.user.UserCreationScreen

internal fun NavGraphBuilder.mainGraph(
    navController: NavController,
) {
    navigation(
        route = NavGraph.MainGraph.route,
        startDestination = NavGraph.MainGraph.startDestination,
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(route = Screen.UserCreation.route) {
            UserCreationScreen(navController = navController)
        }
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
    }
}