package hardcoder.dev.androidApp.ui.navigation

import android.os.Bundle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import hardcoder.dev.logic.hero.gender.Gender

sealed class Screen(val route: String) {
    object Root : Screen("splash")
    object Welcome : Screen("welcome")
    object SelectGender : Screen("select_gender")
    object EnterWeight : Screen("enter_weight/{gender}") {
        fun buildRoute(gender: Gender) = "enter_weight/${gender.name}"
        fun getGender(arguments: Bundle?) = requireNotNull(arguments).getString("gender")!!
        val arguments = listOf(navArgument("gender") { type = NavType.StringType })
    }

    object EnterExerciseStress : Screen("enter_exercise_stress/{gender}/{weight}") {
        fun buildRoute(gender: Gender, weight: Int) =
            "enter_exercise_stress/${gender.name}/${weight}"

        fun getGender(arguments: Bundle?) = requireNotNull(arguments).getString("gender")!!
        fun getWeight(arguments: Bundle?) = requireNotNull(arguments).getInt("weight")
        fun getExerciseStressTime(arguments: Bundle?) =
            requireNotNull(arguments).getInt("exerciseStressTime")

        val arguments = listOf(
            navArgument("gender") { type = NavType.StringType },
            navArgument("weight") { type = NavType.IntType }
        )
    }

    object Dashboard : Screen("dashboard")
    object Settings : Screen("settings")

    object Diary : Screen("diary")
    object DiaryCreateTrack : Screen("diary_create_track")
    object DiaryUpdateTrack : Screen("diary_update_track/{diaryTrackId}") {
        fun buildRoute(diaryTrackId: Int) = "diary_update_track/$diaryTrackId"
        fun getDiaryTrackId(arguments: Bundle?) = requireNotNull(arguments).getInt("diaryTrackId")
        val arguments = listOf(navArgument("diaryTrackId") { type = NavType.IntType })
    }

    object ManageDiaryTags : Screen("manage_diary_tags")
    object CreateDiaryTag : Screen("create_diary_tag")
    object UpdateDiaryTag : Screen("update_diary_tag/{diaryTagId}") {
        fun buildRoute(diaryTagId: Int) = "update_diary_tag/$diaryTagId"
        fun getDiaryTagId(arguments: Bundle?) = requireNotNull(arguments).getInt("diaryTagId")
        val arguments = listOf(navArgument("diaryTagId") { type = NavType.IntType })
    }

    object WaterTrackingFeature : Screen("water_tracking_feature")
    object WaterTrackingHistory : Screen("water_tracking_history")
    object SaveWaterTrack : Screen("save_water_track")
    object UpdateWaterTrack : Screen("update_water_track/{waterTrackId}") {
        fun buildRoute(waterTrackId: Int) = "update_water_track/$waterTrackId"
        fun getWaterTrackId(arguments: Bundle?) = requireNotNull(arguments).getInt("waterTrackId")
        val arguments = listOf(navArgument("waterTrackId") { type = NavType.IntType })
    }

    object ManageDrinkTypes : Screen("manage_drink_type")
    object CreateDrinkType : Screen("create_drink_type")
    object UpdateDrinkType : Screen("update_drink_type/{drinkTypeId}") {
        fun buildRoute(drinkTypeId: Int) = "update_drink_type/$drinkTypeId"
        fun getDrinkTypeId(arguments: Bundle?) = requireNotNull(arguments).getInt("drinkTypeId")
        val arguments = listOf(navArgument("drinkTypeId") { type = NavType.IntType })
    }

    object PedometerFeature : Screen("pedometer_feature") {
        val deepLinks = listOf(
            navDeepLink {
                uriPattern = "healther://pedometer_feature"
            }
        )
    }

    object PedometerHistory : Screen("pedometer_history")

    object FastingFeature : Screen("fasting_feature")
    object FastingCreateTrack : Screen("fasting_create_track")
    object FastingHistory : Screen("fasting_history")

    object MoodTrackingFeature : Screen("mood_tracking_feature")
    object MoodTrackingHistory : Screen("mood_tracking_history")
    object MoodTrackingCreate : Screen("mood_tracking_create")
    object MoodTrackingUpdate : Screen("mood_tracking_update/{moodTrackId}") {
        fun buildRoute(moodTrackId: Int) = "mood_tracking_update/$moodTrackId"
        fun getMoodTrackId(arguments: Bundle?) = requireNotNull(arguments).getInt("moodTrackId")
        val arguments = listOf(navArgument("moodTrackId") { type = NavType.IntType })
    }

    object ManageMoodTypes : Screen("manage_mood_types")
    object MoodTypeCreate : Screen("mood_type_create")
    object MoodTypeUpdate : Screen("mood_type_update/{moodTypeId}") {
        fun buildRoute(moodTypeId: Int) = "mood_type_update/$moodTypeId"
        fun getMoodTypeId(arguments: Bundle?) = requireNotNull(arguments).getInt("moodTypeId")
        val arguments = listOf(navArgument("moodTypeId") { type = NavType.IntType })
    }

    object ManageActivities : Screen("manage_activities")
    object ActivityCreate : Screen("activity_create")
    object ActivityUpdate : Screen("activity_update/{activityId}") {
        fun buildRoute(hobbyTrackId: Int) = "activity_update/$hobbyTrackId"
        fun getActivityIdId(arguments: Bundle?) = requireNotNull(arguments).getInt("activityId")
        val arguments = listOf(navArgument("activityId") { type = NavType.IntType })
    }
}