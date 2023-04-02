package hardcoder.dev.androidApp.ui.navigation

sealed class Flow(val startDestination: String, val route: String) {
    object SetUpFlow : Flow(Screen.Welcome.route, "set_up_flow")
    object WaterTrackingFlow : Flow(Screen.WaterTrackingFeature.route, "water_tracking_flow")
    object PedometerFlow : Flow(Screen.PedometerFeature.route, "pedometer_flow")
    object FastingFlow : Flow(Screen.FastingFeature.route, "fasting_flow")
    object MoodTrackingFlow : Flow(Screen.MoodTrackingFeature.route, "mood_tracking_flow")
}