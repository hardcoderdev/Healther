package hardcoder.dev.navigation.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import hardcoder.dev.navigation.graphs.features.diaryGraph
import hardcoder.dev.navigation.graphs.features.foodTrackingGraph
import hardcoder.dev.navigation.graphs.features.moodTrackingGraph
import hardcoder.dev.navigation.graphs.features.pedometerGraph
import hardcoder.dev.navigation.graphs.features.waterTrackingGraph
import hardcoder.dev.navigation.graphs.mainFlow.mainGraph
import hardcoder.dev.navigation.routes.NavGraph

@Composable
fun RootScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        route = NavGraph.AppGraph.route,
        startDestination = NavGraph.MainGraph.route,
    ) {
        mainGraph(navController = navController)
        waterTrackingGraph(navController = navController)
        moodTrackingGraph(navController = navController)
        foodTrackingGraph(navController = navController)
        pedometerGraph(navController = navController)
        diaryGraph(navController = navController)
    }
}