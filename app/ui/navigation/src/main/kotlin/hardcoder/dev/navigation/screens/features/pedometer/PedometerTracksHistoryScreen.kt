package hardcoder.dev.navigation.screens.features.pedometer

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.formatters.DecimalFormatter
import hardcoder.dev.presentation.features.pedometer.PedometerHistoryViewModel
import hardcoder.dev.screens.features.pedometer.history.PedometerHistory
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
internal fun PedometerTracksHistoryScreen(navController: NavController) {
    val decimalFormatter = koinInject<DecimalFormatter>()
    val dateTimeProvider = koinInject<DateTimeProvider>()
    val viewModel = koinViewModel<PedometerHistoryViewModel>()

    PedometerHistory(
        decimalFormatter = decimalFormatter,
        dateTimeProvider = dateTimeProvider,
        dateRangeInputController = viewModel.dateRangeInputController,
        statisticLoadingController = viewModel.statisticLoadingController,
        chartEntriesLoadingController = viewModel.chartEntriesLoadingController,
        onGoBack = navController::popBackStack,
    )
}