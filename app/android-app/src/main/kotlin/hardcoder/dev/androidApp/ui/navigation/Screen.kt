package hardcoder.dev.androidApp.ui.navigation

import android.os.Bundle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import hardcoder.dev.entities.hero.Gender

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

    object WaterTrackingFeature : Screen("water_tracking_feature")
    object WaterTrackingHistory : Screen("water_tracking_history")
    object SaveWaterTrack : Screen("save_water_track")
    object UpdateWaterTrack : Screen("update_water_track/{waterTrackId}") {
        fun buildRoute(waterTrackId: Int) = "update_water_track/$waterTrackId"
        fun getWaterTrackId(arguments: Bundle?) = requireNotNull(arguments).getInt("waterTrackId")
        val arguments = listOf(navArgument("waterTrackId") { type = NavType.IntType })
    }

    object PedometerFeature : Screen("pedometer_feature") {
        val deepLinks = listOf(
            navDeepLink {
                uriPattern = "healther://pedometer_feature"
            }
        )
    }

    object PedometerHistory : Screen("pedometer_history")

    object StarvationFeature : Screen("starvation_feature")
    object StarvationCreateTrack : Screen("starvation_create_track")
    object StarvationHistory : Screen("starvation_history")
}