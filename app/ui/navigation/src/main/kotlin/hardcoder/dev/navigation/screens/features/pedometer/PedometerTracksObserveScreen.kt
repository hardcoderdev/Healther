package hardcoder.dev.navigation.screens.features.pedometer

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.formatters.DecimalFormatter
import hardcoder.dev.formatters.MillisDistanceFormatter
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.pedometer_manager.permissions.PedometerRejectedMapper
import hardcoder.dev.presentation.features.pedometer.PedometerViewModel
import hardcoder.dev.resolvers.features.pedometer.PedometerStatisticResolver
import hardcoder.dev.screens.features.pedometer.Pedometer
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
internal fun PedometerTracksObserveScreen(navController: NavController) {
    val pedometerRejectedMapper = koinInject<PedometerRejectedMapper>()
    val millisDistanceFormatter = koinInject<MillisDistanceFormatter>()
    val decimalFormatter = koinInject<DecimalFormatter>()
    val pedometerStatisticResolver = koinInject<PedometerStatisticResolver>()
    val viewModel = koinViewModel<PedometerViewModel>()

    Pedometer(
        pedometerStatisticResolver = pedometerStatisticResolver,
        millisDistanceFormatter = millisDistanceFormatter,
        decimalFormatter = decimalFormatter,
        pedometerRejectedMapper = pedometerRejectedMapper,
        chartEntriesLoadingController = viewModel.chartEntriesLoadingController,
        dailyRateStepsLoadingController = viewModel.dailyRateStepsLoadingController,
        pedometerAvailabilityLoadingController = viewModel.pedometerAvailabilityLoadingController,
        todayStatisticLoadingController = viewModel.todayStatisticLoadingController,
        statisticLoadingController = viewModel.statisticLoadingController,
        pedometerToggleController = viewModel.pedometerToggleController,
        dailyRateProgressController = viewModel.dailyRateProgressController,
        onGoBack = navController::popBackStack,
        onGoToHistory = {
            navController.navigate(Screen.PedometerTracksHistory.route)
        },
    )
}