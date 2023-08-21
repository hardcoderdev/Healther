package hardcoder.dev.androidApp.ui.screens.features.waterTracking.waterTrack.history

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
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.getEndOfDay
import hardcoder.dev.datetime.getStartOfDay
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingHistoryViewModel
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingItem
import hardcoder.dev.uikit.components.calendar.SingleSelectionCalendar
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoderdev.healther.app.android.app.R
import kotlinx.datetime.Instant
import org.koin.compose.koinInject
import hardcoder.dev.androidApp.ui.screens.features.waterTracking.waterTrack.WaterTrackItem as WaterTrackItemRow

@Composable
fun WaterTrackingHistory(
    viewModel: WaterTrackingHistoryViewModel,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            WaterTrackingHistoryContent(
                dateRangeInputController = viewModel.dateRangeInputController,
                itemsLoadingController = viewModel.waterTracksLoadingController,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.waterTracking_history_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun WaterTrackingHistoryContent(
    dateRangeInputController: InputController<ClosedRange<Instant>>,
    itemsLoadingController: LoadingController<List<WaterTrackingItem>>,
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
        WaterTracksHistory(
            itemsLoadingController = itemsLoadingController,
        )
    }
}

@Composable
private fun WaterTracksHistory(
    itemsLoadingController: LoadingController<List<WaterTrackingItem>>,
) {
    LoadingContainer(controller = itemsLoadingController) { waterTrackingItems ->
        if (waterTrackingItems.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
            ) {
                items(waterTrackingItems) { track ->
                    WaterTrackItemRow(
                        waterTrackingItem = track,
                        onUpdate = {
                            /* no-op because money for track has already been collected */
                        },
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.height(16.dp))
            Description(text = stringResource(id = R.string.waterTracking_history_emptyDayHistory_text))
        }
    }
}