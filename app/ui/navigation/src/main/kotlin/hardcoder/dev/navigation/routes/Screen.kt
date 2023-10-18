package hardcoder.dev.navigation.routes

import android.os.Bundle

internal sealed class Screen(val route: String) {

    // Main screens
    data object Splash : Screen(SPLASH_ROUTE)
    data object UserCreation : Screen(HERO_CREATION_ROUTE)
    data object Dashboard : Screen(DASHBOARD_ROUTE)
    data object Settings : Screen(SETTINGS_ROUTE)

    // Water tracking feature
    data object WaterTracksObserve : Screen(WATER_TRACKS_OBSERVE)
    data object WaterTracksCreate : Screen(WATER_TRACKS_CREATE)
    data object WaterTracksUpdate : Screen(WATER_TRACKS_UPDATE) {
        fun buildRoute(waterTrackId: Int) = WATER_TRACKS_UPDATE.replace(WATER_TRACK_ID_ARG, waterTrackId.toString())
        fun getWaterTrackId(arguments: Bundle?) = requireNotNull(arguments).getInt(WATER_TRACK_ID_ARG)
    }

    data object WaterTracksHistory : Screen(WATER_TRACKS_HISTORY)
    data object WaterTracksAnalytics : Screen(WATER_TRACKS_ANALYTICS)

    data object DrinkTypesObserve : Screen(DRINK_TYPES_OBSERVE)
    data object DrinkTypesCreate : Screen(DRINK_TYPES_CREATE)
    data object DrinkTypesUpdate : Screen(DRINK_TYPES_UPDATE) {
        fun buildRoute(drinkTypeId: Int) = DRINK_TYPES_UPDATE.replace(DRINK_TYPE_ID_ARG, drinkTypeId.toString())
        fun getDrinkTypeId(arguments: Bundle?) = requireNotNull(arguments).getInt(DRINK_TYPE_ID_ARG)
    }

    // Pedometer feature
    data object PedometerTracksObserve : Screen(PEDOMETER_TRACKS_OBSERVE)
    data object PedometerTracksHistory : Screen(PEDOMETER_TRACKS_HISTORY)

    // Mood tracking feature
    data object MoodTracksObserve : Screen(MOOD_TRACKS_OBSERVE)
    data object MoodTracksCreate : Screen(MOOD_TRACKS_CREATE)
    data object MoodTracksUpdate : Screen(MOOD_TRACKS_UPDATE) {
        fun buildRoute(moodTrackId: Int) = MOOD_TRACKS_UPDATE.replace(MOOD_TRACK_ID_ARG, moodTrackId.toString())
        fun getMoodTrackId(arguments: Bundle?) = requireNotNull(arguments).getInt(MOOD_TRACK_ID_ARG)
    }

    data object MoodTracksAnalytics : Screen(MOOD_TRACKS_ANALYTICS)
    data object MoodTracksHistory : Screen(MOOD_TRACKS_HISTORY)

    data object MoodTypesObserve : Screen(MOOD_TYPES_OBSERVE)
    data object MoodTypesCreate : Screen(MOOD_TYPES_CREATE)
    data object MoodTypesUpdate : Screen(MOOD_TYPES_UPDATE) {
        fun buildRoute(moodTypeId: Int) = MOOD_TYPES_UPDATE.replace(MOOD_TYPE_ID_ARG, moodTypeId.toString())
        fun getMoodTypeId(arguments: Bundle?) = requireNotNull(arguments).getInt(MOOD_TYPE_ID_ARG)
    }

    data object MoodActivitiesObserve : Screen(MOOD_ACTIVITIES_OBSERVE)
    data object MoodActivitiesCreate : Screen(MOOD_ACTIVITIES_CREATE)
    data object MoodActivitiesUpdate : Screen(MOOD_ACTIVITIES_UPDATE) {
        fun buildRoute(moodActivityId: Int) = MOOD_ACTIVITIES_UPDATE.replace(MOOD_ACTIVITY_ID_ARG, moodActivityId.toString())
        fun getMoodActivityId(arguments: Bundle?) = requireNotNull(arguments).getInt(MOOD_ACTIVITY_ID_ARG)
    }

    // Food tracking feature
    data object FoodTracksObserve : Screen(FOOD_TRACKS_OBSERVE)
    data object FoodTracksCreate : Screen(FOOD_TRACKS_CREATE)
    data object FoodTracksUpdate : Screen(FOOD_TRACKS_UPDATE) {
        fun buildRoute(foodTrackId: Int) = FOOD_TRACKS_UPDATE.replace(FOOD_TRACK_ID_ARG, foodTrackId.toString())
        fun getFoodTrackIdId(arguments: Bundle?) = requireNotNull(arguments).getInt(FOOD_TRACK_ID_ARG)
    }

    data object FoodTypesObserve : Screen(FOOD_TYPES_OBSERVE)
    data object FoodTypesCreate : Screen(FOOD_TYPES_CREATE)
    data object FoodTypesUpdate : Screen(FOOD_TYPES_UPDATE) {
        fun buildRoute(foodTypeId: Int) = FOOD_TYPES_UPDATE.replace(FOOD_TYPE_ID_ARG, foodTypeId.toString())
        fun getFoodTypeId(arguments: Bundle?) = requireNotNull(arguments).getInt(FOOD_TYPE_ID_ARG)
    }

    // Diary feature
    data object DiaryTracksObserve : Screen(DIARY_TRACKS_OBSERVE)
    data object DiaryTracksCreate : Screen(DIARY_TRACKS_CREATE)
    data object DiaryTracksUpdate : Screen(DIARY_TRACKS_UPDATE) {
        fun buildRoute(diaryTrackId: Int) = DIARY_TRACKS_UPDATE.replace(DIARY_TRACK_ID, diaryTrackId.toString())
        fun getDiaryTrackId(arguments: Bundle?) = requireNotNull(arguments).getInt(DIARY_TRACK_ID)
    }

    data object DiaryTagsObserve : Screen(DIARY_TAGS_OBSERVE)
    data object DiaryTagsCreate : Screen(DIARY_TAGS_CREATE)
    data object DiaryTagsUpdate : Screen(DIARY_TAGS_UPDATE) {
        fun buildRoute(diaryTagId: Int) = DIARY_TAGS_UPDATE.replace(DIARY_TAG_ID, diaryTagId.toString())
        fun getDiaryTagIdId(arguments: Bundle?) = requireNotNull(arguments).getInt(DIARY_TAG_ID)
    }
}

// Arguments
private const val DRINK_TYPE_ID_ARG = "drinkTypeId"
private const val WATER_TRACK_ID_ARG = "waterTrackId"
private const val MOOD_TRACK_ID_ARG = "moodTrackId"
private const val MOOD_TYPE_ID_ARG = "moodTypeId"
private const val MOOD_ACTIVITY_ID_ARG = "moodActivityId"
private const val FOOD_TRACK_ID_ARG = "foodTrackId"
private const val FOOD_TYPE_ID_ARG = "foodTypeId"
private const val DIARY_TRACK_ID = "diaryTrackId"
private const val DIARY_TAG_ID = "diaryTagId"

// Main screens

private const val SPLASH_ROUTE = "splash_screen"
private const val HERO_CREATION_ROUTE = "hero_creation_screen"
private const val DASHBOARD_ROUTE = "dashboard_screen"
private const val SETTINGS_ROUTE = "settings_screen"

// Water tracking feature
private const val WATER_TRACKS_OBSERVE = "water_tracks_observe_screen"
private const val WATER_TRACKS_CREATE = "water_tracks_create_screen"
private const val WATER_TRACKS_UPDATE = "water_tracks_update_screen/{waterTrackId}"
private const val WATER_TRACKS_HISTORY = "water_tracks_history"
private const val WATER_TRACKS_ANALYTICS = "water_tracks_analytics"
private const val DRINK_TYPES_OBSERVE = "drink_types_observe_screen"
private const val DRINK_TYPES_CREATE = "drink_types_create_screen"
private const val DRINK_TYPES_UPDATE = "drink_types_update_screen/{drinkTypeId}"

// Pedometer feature
private const val PEDOMETER_TRACKS_OBSERVE = "pedometer_tracks_observe_screen"
private const val PEDOMETER_TRACKS_HISTORY = "pedometer_tracks_history_screen"

// Mood tracking feature
private const val MOOD_TRACKS_OBSERVE = "mood_tracks_observe_screen"
private const val MOOD_TRACKS_CREATE = "mood_tracks_create_screen"
private const val MOOD_TRACKS_UPDATE = "mood_tracks_update_screen/{moodTrackId}"
private const val MOOD_TRACKS_ANALYTICS = "mood_tracks_analytics_screen"
private const val MOOD_TRACKS_HISTORY = "mood_tracks_history_screen"
private const val MOOD_TYPES_OBSERVE = "mood_types_observe_screen"
private const val MOOD_TYPES_CREATE = "mood_types_create_screen"
private const val MOOD_TYPES_UPDATE = "mood_types_update_screen/{moodTypeId}"
private const val MOOD_ACTIVITIES_OBSERVE = "mood_activities_observe_screen"
private const val MOOD_ACTIVITIES_CREATE = "mood_activities_create_screen"
private const val MOOD_ACTIVITIES_UPDATE = "mood_activities_update_screen/{moodActivityId}"

// Food tracking feature
private const val FOOD_TRACKS_OBSERVE = "food_tracks_observe_screen"
private const val FOOD_TRACKS_CREATE = "food_tracks_create_screen"
private const val FOOD_TRACKS_UPDATE = "food_tracks_update_screen/{foodTrackId}"
private const val FOOD_TYPES_OBSERVE = "food_types_observe_screen"
private const val FOOD_TYPES_CREATE = "food_types_create_screen"
private const val FOOD_TYPES_UPDATE = "food_types_update_screen/{foodTypeId}"

// Diary feature
private const val DIARY_TRACKS_OBSERVE = "diary_tracks_observe_screen"
private const val DIARY_TRACKS_CREATE = "diary_tracks_create_screen"
private const val DIARY_TRACKS_UPDATE = "diary_tracks_update_screen/{diaryTrackId}"
private const val DIARY_TAGS_OBSERVE = "diary_tags_observe_screens"
private const val DIARY_TAGS_CREATE = "diary_tags_create_screen"
private const val DIARY_TAGS_UPDATE = "diary_tags_update_screen/{diaryTagId}"