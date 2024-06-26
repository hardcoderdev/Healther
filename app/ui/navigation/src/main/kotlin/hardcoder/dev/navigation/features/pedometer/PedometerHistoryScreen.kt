package hardcoder.dev.navigation.features.pedometer

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.formatters.DecimalFormatter
import hardcoder.dev.presentation.features.pedometer.PedometerHistoryViewModel
import hardcoder.dev.screens.features.pedometer.history.PedometerHistory
import org.koin.compose.koinInject

class PedometerHistoryScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<PedometerHistoryViewModel>()
        val decimalFormatter = koinInject<DecimalFormatter>()
        val dateTimeProvider = koinInject<DateTimeProvider>()

        PedometerHistory(
            decimalFormatter = decimalFormatter,
            dateTimeProvider = dateTimeProvider,
            dateRangeInputController = viewModel.dateRangeInputController,
            statisticLoadingController = viewModel.statisticLoadingController,
            chartEntriesLoadingController = viewModel.chartEntriesLoadingController,
            onGoBack = navigator::pop,
        )
    }
}