package hardcoder.dev.navigation.graphs.features

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import hardcoder.dev.navigation.routes.NavGraph
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.navigation.screens.features.pedometer.PedometerTracksHistoryScreen
import hardcoder.dev.navigation.screens.features.pedometer.PedometerTracksObserveScreen

internal fun NavGraphBuilder.pedometerGraph(
    navController: NavController,
) {
    navigation(
        route = NavGraph.PedometerGraph.route,
        startDestination = NavGraph.PedometerGraph.startDestination,
    ) {
        composable(route = Screen.PedometerTracksObserve.route) {
            PedometerTracksObserveScreen(navController = navController)
        }
        composable(route = Screen.PedometerTracksHistory.route) {
            PedometerTracksHistoryScreen(navController = navController)
        }
    }
}