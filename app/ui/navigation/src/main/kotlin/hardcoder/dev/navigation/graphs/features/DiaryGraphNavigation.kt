package hardcoder.dev.navigation.graphs.features

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import hardcoder.dev.navigation.routes.NavGraph
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.navigation.screens.features.diary.diaryTags.DiaryTagsCreationScreen
import hardcoder.dev.navigation.screens.features.diary.diaryTags.DiaryTagsObserveScreen
import hardcoder.dev.navigation.screens.features.diary.diaryTags.DiaryTagsUpdateScreen
import hardcoder.dev.navigation.screens.features.diary.diaryTracks.DiaryTracksCreateScreen
import hardcoder.dev.navigation.screens.features.diary.diaryTracks.DiaryTracksObserveScreen
import hardcoder.dev.navigation.screens.features.diary.diaryTracks.DiaryTracksUpdateScreen

internal fun NavGraphBuilder.diaryGraph(
    navController: NavController,
) {
    navigation(
        route = NavGraph.DiaryGraph.route,
        startDestination = NavGraph.DiaryGraph.startDestination,
    ) {
        composable(route = Screen.DiaryTracksObserve.route) {
            DiaryTracksObserveScreen(navController = navController)
        }
        composable(route = Screen.DiaryTracksCreate.route) {
            DiaryTracksCreateScreen(navController = navController)
        }
        composable(route = Screen.DiaryTracksUpdate.route) {
            DiaryTracksUpdateScreen(
                diaryTrackId = Screen.DiaryTracksUpdate.getDiaryTrackId(it.arguments),
                navController = navController,
            )
        }

        // Diary tags
        composable(route = Screen.DiaryTagsObserve.route) {
            DiaryTagsObserveScreen(navController = navController)
        }
        composable(route = Screen.DiaryTagsCreate.route) {
            DiaryTagsCreationScreen(navController = navController)
        }
        composable(route = Screen.DiaryTagsUpdate.route) {
            DiaryTagsUpdateScreen(
                diaryTagId = Screen.DiaryTagsUpdate.getDiaryTagIdId(it.arguments),
                navController = navController,
            )
        }
    }
}