package hardcoder.dev.androidApp.ui.navigation.features.fasting

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.formatters.MillisDistanceFormatter
import hardcoder.dev.presentation.features.fasting.FastingHistoryViewModel
import hardcoder.dev.resources.features.fasting.FastingPlanResourcesProvider
import hardcoder.dev.screens.features.fasting.history.FastingHistory
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

class FastingHistoryScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<FastingHistoryViewModel>()
        val millisDistanceFormatter = koinInject<MillisDistanceFormatter>()
        val dateTimeProvider = koinInject<DateTimeProvider>()
        val dateTimeFormatter = koinInject<DateTimeFormatter>()
        val fastingPlanResourcesProvider = koinInject<FastingPlanResourcesProvider>()

        FastingHistory(
            millisDistanceFormatter = millisDistanceFormatter,
            dateTimeProvider = dateTimeProvider,
            dateTimeFormatter = dateTimeFormatter,
            fastingPlanResourcesProvider = fastingPlanResourcesProvider,
            fastingTracksLoadingController = viewModel.fastingTracksLoadingController,
            dateRangeInputController = viewModel.dateRangeInputController,
            onGoBack = navigator::pop,
        )
    }
}