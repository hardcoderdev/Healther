package hardcoder.dev.navigation.graphs.features

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import hardcoder.dev.navigation.routes.NavGraph
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.navigation.screens.features.waterTracking.drinkTypes.DrinkTypesCreateScreen
import hardcoder.dev.navigation.screens.features.waterTracking.drinkTypes.DrinkTypesObserveScreen
import hardcoder.dev.navigation.screens.features.waterTracking.drinkTypes.DrinkTypesUpdate
import hardcoder.dev.navigation.screens.features.waterTracking.waterTracks.WaterTracksAnalyticsScreen
import hardcoder.dev.navigation.screens.features.waterTracking.waterTracks.WaterTracksCreateScreen
import hardcoder.dev.navigation.screens.features.waterTracking.waterTracks.WaterTracksHistoryScreen
import hardcoder.dev.navigation.screens.features.waterTracking.waterTracks.WaterTracksObserveScreen
import hardcoder.dev.navigation.screens.features.waterTracking.waterTracks.WaterTracksUpdateScreen

internal fun NavGraphBuilder.waterTrackingGraph(
    navController: NavController,
) {
    navigation(
        route = NavGraph.WaterTrackingGraph.route,
        startDestination = NavGraph.WaterTrackingGraph.startDestination,
    ) {
        composable(route = Screen.WaterTracksObserve.route) {
            WaterTracksObserveScreen(navController = navController)
        }
        composable(route = Screen.WaterTracksCreate.route) {
            WaterTracksCreateScreen(navController = navController)
        }
        composable(
            route = Screen.WaterTracksUpdate.route,
            arguments = Screen.WaterTracksUpdate.arguments,
        ) {
            WaterTracksUpdateScreen(
                waterTrackId = Screen.WaterTracksUpdate.getWaterTrackId(it.arguments),
                navController = navController,
            )
        }
        composable(route = Screen.WaterTracksHistory.route) {
            WaterTracksHistoryScreen(navController = navController)
        }
        composable(route = Screen.WaterTracksAnalytics.route) {
            WaterTracksAnalyticsScreen(navController = navController)
        }

        // Drink types
        composable(route = Screen.DrinkTypesObserve.route) {
            DrinkTypesObserveScreen(navController = navController)
        }
        composable(route = Screen.DrinkTypesCreate.route) {
            DrinkTypesCreateScreen(navController = navController)
        }
        composable(
            route = Screen.DrinkTypesUpdate.route,
            arguments = Screen.DrinkTypesUpdate.arguments,
        ) {
            DrinkTypesUpdate(
                drinkTypeId = Screen.DrinkTypesUpdate.getDrinkTypeId(it.arguments),
                navController = navController,
            )
        }
    }
}