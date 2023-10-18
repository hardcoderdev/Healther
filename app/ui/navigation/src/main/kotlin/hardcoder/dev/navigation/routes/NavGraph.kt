package hardcoder.dev.navigation.routes

internal sealed class NavGraph(
    val route: String,
    val startDestination: String,
) {
    data object AppGraph : NavGraph(
        route = APP_GRAPH_ROUTE,
        startDestination = MAIN_GRAPH_ROUTE,
    )

    data object MainGraph : NavGraph(
        route = MAIN_GRAPH_ROUTE,
        startDestination = Screen.Splash.route,
    )

    data object WaterTrackingGraph : NavGraph(
        route = WATER_TRACKING_GRAPH,
        startDestination = Screen.WaterTracksObserve.route,
    )

    data object MoodTrackingGraph : NavGraph(
        route = MOOD_TRACKING_GRAPH,
        startDestination = Screen.MoodTracksObserve.route,
    )

    data object FoodTrackingGraph : NavGraph(
        route = FOOD_TRACKING_GRAPH,
        startDestination = Screen.FoodTracksObserve.route,
    )

    data object PedometerGraph : NavGraph(
        route = PEDOMETER_GRAPH,
        startDestination = Screen.PedometerTracksObserve.route,
    )

    data object DiaryGraph : NavGraph(
        route = DIARY_GRAPH,
        startDestination = Screen.DiaryTracksObserve.route,
    )
}

// Routes to graphs
private const val APP_GRAPH_ROUTE = "app_graph"
private const val MAIN_GRAPH_ROUTE = "main_graph"
private const val WATER_TRACKING_GRAPH = "water_tracking_graph"
private const val MOOD_TRACKING_GRAPH = "mood_tracking_graph"
private const val FOOD_TRACKING_GRAPH = "food_tracking_graph"
private const val PEDOMETER_GRAPH = "pedometer_graph"
private const val DIARY_GRAPH = "diary_graph"