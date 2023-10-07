package hardcoder.dev.androidApp.ui.navigation.features.fasting

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.screens.features.fasting.FastingInitial
import hardcoder.dev.androidApp.ui.screens.features.fasting.plans.FastingPlanResourcesProvider
import hardcoder.dev.androidApp.ui.screens.features.fasting.statistics.FastingStatisticResolver
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.formatters.MillisDistanceFormatter
import hardcoder.dev.presentation.features.fasting.FastingViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

class FastingScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<FastingViewModel>()
        val millisDistanceFormatter = koinInject<MillisDistanceFormatter>()
        val dateTimeFormatter = koinInject<DateTimeFormatter>()
        val fastingPlanResourcesProvider = koinInject<FastingPlanResourcesProvider>()
        val fastingStatisticResolver = koinInject<FastingStatisticResolver>()

        FastingInitial(
            fastingStatisticResolver = fastingStatisticResolver,
            dateTimeFormatter = dateTimeFormatter,
            fastingPlanResourcesProvider = fastingPlanResourcesProvider,
            millisDistanceFormatter = millisDistanceFormatter,
            collectRewardController = viewModel.collectRewardController,
            createRewardController = viewModel.createRewardController,
            rewardLoadingController = viewModel.rewardLoadingController,
            lastThreeFastingTracksLoadingController = viewModel.lastThreeFastingTrackLoadingController,
            noteInputController = viewModel.noteInputController,
            fastingStateLoadingController = viewModel.fastingStateLoadingController,
            interruptFastingController = viewModel.interruptFastingController,
            fastingStatisticsLoadingController = viewModel.statisticLoadingController,
            fastingChartDataLoadingController = viewModel.chartEntriesLoadingController,
            onGoBack = navigator::pop,
            onCreateFastingTrack = {
                navigator += FastingCreationScreen()
            },
            onGoToFastingHistory = {
                navigator += FastingHistoryScreen()
            },
        )
    }
}