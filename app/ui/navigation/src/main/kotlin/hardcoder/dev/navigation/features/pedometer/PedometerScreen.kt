package hardcoder.dev.navigation.features.pedometer

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.formatters.DecimalFormatter
import hardcoder.dev.formatters.MillisDistanceFormatter
import hardcoder.dev.pedometer_manager.permissions.PedometerRejectedMapper
import hardcoder.dev.presentation.features.pedometer.PedometerViewModel
import hardcoder.dev.resolvers.features.pedometer.PedometerStatisticResolver
import hardcoder.dev.screens.features.pedometer.Pedometer
import org.koin.compose.koinInject

class PedometerScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<PedometerViewModel>()
        val pedometerRejectedMapper = koinInject<PedometerRejectedMapper>()
        val millisDistanceFormatter = koinInject<MillisDistanceFormatter>()
        val decimalFormatter = koinInject<DecimalFormatter>()
        val pedometerStatisticResolver = koinInject<PedometerStatisticResolver>()

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
            onGoBack = navigator::pop,
            onGoToHistory = {
                navigator += PedometerHistoryScreen()
            },
        )
    }
}