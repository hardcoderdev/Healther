package hardcoder.dev.androidApp.ui.navigation

sealed class NavGraph(val startDestination: String, val route: String) {
    object SetUpNavGraph : NavGraph(Screen.Welcome.route, "set_up_nav_graph")
    object WaterTrackingNavGraph : NavGraph(Screen.WaterTrackingFeature.route, "water_tracking_nav_graph")
    object PedometerNavGraph : NavGraph(Screen.PedometerFeature.route, "pedometer_nav_graph")
    object FastingNavGraph : NavGraph(Screen.FastingFeature.route, "fasting_nav_graph")
    object MoodTrackingNavGraph : NavGraph(Screen.MoodTrackingFeature.route, "mood_tracking_nav_graph")
    object DiaryNavGraph : NavGraph(Screen.Diary.route, "diary_nav_graph")
}