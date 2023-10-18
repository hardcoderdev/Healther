package hardcoder.dev.navigation.graphs.features

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import hardcoder.dev.navigation.routes.NavGraph
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.navigation.screens.features.foodTracking.foodTracks.FoodTracksCreateScreen
import hardcoder.dev.navigation.screens.features.foodTracking.foodTracks.FoodTracksObserveScreen
import hardcoder.dev.navigation.screens.features.foodTracking.foodTracks.FoodTracksUpdateScreen
import hardcoder.dev.navigation.screens.features.foodTracking.foodTypes.FoodTypesCreateScreen
import hardcoder.dev.navigation.screens.features.foodTracking.foodTypes.FoodTypesObserveScreen
import hardcoder.dev.navigation.screens.features.foodTracking.foodTypes.FoodTypesUpdateScreen

internal fun NavGraphBuilder.foodTrackingGraph(
    navController: NavController,
) {
    navigation(
        route = NavGraph.FoodTrackingGraph.route,
        startDestination = NavGraph.FoodTrackingGraph.startDestination,
    ) {
        composable(route = Screen.FoodTracksObserve.route) {
            FoodTracksObserveScreen(navController = navController)
        }
        composable(route = Screen.FoodTracksCreate.route) {
            FoodTracksCreateScreen(navController = navController)
        }
        composable(route = Screen.FoodTracksUpdate.route) {
            FoodTracksUpdateScreen(
                foodTrackId = Screen.FoodTracksUpdate.getFoodTrackIdId(it.arguments),
                navController = navController,
            )
        }

        // Food types
        composable(route = Screen.FoodTypesObserve.route) {
            FoodTypesObserveScreen(navController = navController)
        }
        composable(route = Screen.FoodTypesCreate.route) {
            FoodTypesCreateScreen(navController = navController)
        }
        composable(route = Screen.FoodTypesUpdate.route) {
            FoodTypesUpdateScreen(
                foodTypeId = Screen.FoodTypesUpdate.getFoodTypeId(it.arguments),
                navController = navController,
            )
        }
    }
}