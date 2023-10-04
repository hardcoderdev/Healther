package hardcoder.dev.androidApp.ui.navigation.features.fasting

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.screens.features.fasting.history.FastingHistory
import hardcoder.dev.androidApp.ui.screens.features.fasting.plans.FastingPlanResourcesProvider
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.presentation.features.fasting.FastingHistoryViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

class FastingHistoryScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<FastingHistoryViewModel>()
        val millisDistanceFormatter = koinInject<hardcoder.dev.formatters.MillisDistanceFormatter>()
        val dateTimeProvider = koinInject<DateTimeProvider>()
        val dateTimeFormatter = koinInject<hardcoder.dev.formatters.DateTimeFormatter>()
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