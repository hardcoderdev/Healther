package hardcoder.dev.androidApp.ui.screens.features.fasting.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.screens.features.fasting.FastingItem
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.getEndOfDay
import hardcoder.dev.datetime.getStartOfDay
import hardcoder.dev.logic.features.fasting.track.FastingTrack
import hardcoder.dev.presentation.features.fasting.FastingHistoryViewModel
import hardcoder.dev.uikit.components.calendar.SingleSelectionCalendar
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoderdev.healther.app.android.app.R
import kotlinx.datetime.Instant
import org.koin.compose.koinInject

@Composable
fun FastingHistory(
    viewModel: FastingHistoryViewModel,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            FastingHistoryContent(
                dateRangeInputController = viewModel.dateRangeInputController,
                fastingTracksLoadingController = viewModel.fastingTracksLoadingController,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.fasting_history_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun FastingHistoryContent(
    dateRangeInputController: InputController<ClosedRange<Instant>>,
    fastingTracksLoadingController: LoadingController<List<FastingTrack>>,
) {
    val dateTimeProvider = koinInject<DateTimeProvider>()

    Column(Modifier.padding(16.dp)) {
        SingleSelectionCalendar(
            dateTimeProvider = dateTimeProvider,
            inputChanged = { date ->
                dateRangeInputController.changeInput(
                    date.getStartOfDay()..date.getEndOfDay(),
                )
            },
        )
        Spacer(modifier = Modifier.height(16.dp))
        FastingTracksHistory(fastingTracksLoadingController = fastingTracksLoadingController)
    }
}

@Composable
private fun FastingTracksHistory(fastingTracksLoadingController: LoadingController<List<FastingTrack>>) {
    LoadingContainer(
        controller = fastingTracksLoadingController,
        loadedContent = { fastingTracksList ->
            Spacer(modifier = Modifier.height(16.dp))
            if (fastingTracksList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                ) {
                    items(fastingTracksList) { fastingTrack ->
                        FastingItem(fastingTrack = fastingTrack)
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                Description(text = stringResource(id = R.string.fasting_history_emptyDayHistory_text))
            }
        },
    )
}