package hardcoder.dev.androidApp.ui.navigation.features.pedometer

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.screens.features.pedometer.Pedometer
import hardcoder.dev.androidApp.ui.screens.features.pedometer.PedometerRejectedMapper
import hardcoder.dev.androidApp.ui.screens.features.pedometer.statistic.PedometerStatisticResolver
import hardcoder.dev.formatters.DecimalFormatter
import hardcoder.dev.formatters.MillisDistanceFormatter
import hardcoder.dev.presentation.features.pedometer.PedometerViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

class PedometerScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<PedometerViewModel>()
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
            rewardLoadingController = viewModel.rewardLoadingController,
            collectRewardController = viewModel.collectRewardController,
            onGoBack = navigator::pop,
            onGoToHistory = {
                navigator += PedometerHistoryScreen()
            },
        )
    }
}