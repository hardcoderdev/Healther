package hardcoder.dev.navigation.graphs.features

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import hardcoder.dev.navigation.routes.NavGraph
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.navigation.screens.features.moodTracking.moodActivities.MoodActivitiesCreateScreen
import hardcoder.dev.navigation.screens.features.moodTracking.moodActivities.MoodActivitiesObserveScreen
import hardcoder.dev.navigation.screens.features.moodTracking.moodActivities.MoodActivitiesUpdateScreen
import hardcoder.dev.navigation.screens.features.moodTracking.moodTracks.MoodTracksAnalyticsScreen
import hardcoder.dev.navigation.screens.features.moodTracking.moodTracks.MoodTracksCreateScreen
import hardcoder.dev.navigation.screens.features.moodTracking.moodTracks.MoodTracksHistoryScreen
import hardcoder.dev.navigation.screens.features.moodTracking.moodTracks.MoodTracksObserveScreen
import hardcoder.dev.navigation.screens.features.moodTracking.moodTracks.MoodTracksUpdateScreen
import hardcoder.dev.navigation.screens.features.moodTracking.moodTypes.MoodTypesCreateScreen
import hardcoder.dev.navigation.screens.features.moodTracking.moodTypes.MoodTypesObserveScreen
import hardcoder.dev.navigation.screens.features.moodTracking.moodTypes.MoodTypesUpdateScreen

internal fun NavGraphBuilder.moodTrackingGraph(
    navController: NavController,
) {
    navigation(
        route = NavGraph.MoodTrackingGraph.route,
        startDestination = NavGraph.MoodTrackingGraph.startDestination,
    ) {
        composable(route = Screen.MoodTracksObserve.route) {
            MoodTracksObserveScreen(navController = navController)
        }
        composable(route = Screen.MoodTracksCreate.route) {
            MoodTracksCreateScreen(navController = navController)
        }
        composable(
            route = Screen.MoodTracksUpdate.route,
            arguments = Screen.MoodTracksUpdate.arguments,
        ) {
            MoodTracksUpdateScreen(
                moodTrackId = Screen.MoodTracksUpdate.getMoodTrackId(it.arguments),
                navController = navController,
            )
        }
        composable(route = Screen.MoodTracksAnalytics.route) {
            MoodTracksAnalyticsScreen(navController = navController)
        }
        composable(route = Screen.MoodTracksHistory.route) {
            MoodTracksHistoryScreen(navController = navController)
        }

        // Mood types
        composable(route = Screen.MoodTypesObserve.route) {
            MoodTypesObserveScreen(navController = navController)
        }
        composable(route = Screen.MoodTypesCreate.route) {
            MoodTypesCreateScreen(navController = navController)
        }
        composable(
            route = Screen.MoodTypesUpdate.route,
            arguments = Screen.MoodTypesUpdate.arguments,
        ) {
            MoodTypesUpdateScreen(
                moodTypeId = Screen.MoodTypesUpdate.getMoodTypeId(it.arguments),
                navController = navController,
            )
        }

        // Mood activities
        composable(route = Screen.MoodActivitiesObserve.route) {
            MoodActivitiesObserveScreen(navController = navController)
        }
        composable(route = Screen.MoodActivitiesCreate.route) {
            MoodActivitiesCreateScreen(navController = navController)
        }
        composable(
            route = Screen.MoodActivitiesUpdate.route,
            arguments = Screen.MoodActivitiesUpdate.arguments,
        ) {
            MoodActivitiesUpdateScreen(
                moodActivityId = Screen.MoodActivitiesUpdate.getMoodActivityId(it.arguments),
                navController = navController,
            )
        }
    }
}