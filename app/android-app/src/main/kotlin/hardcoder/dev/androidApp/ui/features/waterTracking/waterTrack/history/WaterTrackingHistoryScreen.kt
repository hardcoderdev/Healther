package hardcoder.dev.androidApp.ui.features.waterTracking.waterTrack.history

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.datetime.createRangeForThisDay
import hardcoder.dev.datetime.getEndOfDay
import hardcoder.dev.datetime.getStartOfDay
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingHistoryViewModel
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingItem
import hardcoder.dev.uikit.LoadingContainer
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.calendar.CustomMonthHeader
import hardcoder.dev.uikit.text.Description
import hardcoderdev.healther.app.android.app.R
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import org.koin.androidx.compose.koinViewModel
import hardcoder.dev.androidApp.ui.features.waterTracking.waterTrack.WaterTrackItem as WaterTrackItemRow

@Composable
fun WaterTrackingHistoryScreen(
    onGoBack: () -> Unit,
    onTrackUpdate: (WaterTrackingItem) -> Unit
) {
    val viewModel = koinViewModel<WaterTrackingHistoryViewModel>()

    ScaffoldWrapper(
        content = {
            WaterTrackingContent(
                onTrackUpdate = onTrackUpdate,
                dateRangeInputController = viewModel.dateRangeInputController,
                itemsLoadingController = viewModel.waterTracksLoadingController
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.waterTracking_history_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun WaterTrackingContent(
    onTrackUpdate: (WaterTrackingItem) -> Unit,
    dateRangeInputController: InputController<ClosedRange<Instant>>,
    itemsLoadingController: LoadingController<List<WaterTrackingItem>>
) {
    val calendarState = rememberSelectableCalendarState(initialSelectionMode = SelectionMode.Single)

    LaunchedEffect(key1 = calendarState.selectionState.selection) {
        if (calendarState.selectionState.selection.isNotEmpty()) {
            val date = calendarState.selectionState.selection.first().toKotlinLocalDate()
            dateRangeInputController.changeInput(date.getStartOfDay()..date.getEndOfDay())
        } else {
            dateRangeInputController.changeInput(LocalDate.now().createRangeForThisDay())
        }
    }

    Column(Modifier.padding(16.dp)) {
        SelectableCalendar(
            calendarState = calendarState,
            monthHeader = { monthState ->
                CustomMonthHeader(monthState = monthState)
                Spacer(modifier = Modifier.height(16.dp))
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        WaterTracksHistory(
            itemsLoadingController = itemsLoadingController,
            onTrackUpdate = onTrackUpdate
        )
    }
}

@Composable
private fun WaterTracksHistory(
    itemsLoadingController: LoadingController<List<WaterTrackingItem>>,
    onTrackUpdate: (WaterTrackingItem) -> Unit
) {
    LoadingContainer(controller = itemsLoadingController) { waterTrackingItems ->
        if (waterTrackingItems.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(waterTrackingItems) { track ->
                    WaterTrackItemRow(
                        waterTrackingItem = track,
                        onUpdate = onTrackUpdate
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.height(16.dp))
            Description(text = stringResource(id = R.string.waterTracking_history_emptyDayHistory_text))
        }
    }
}

