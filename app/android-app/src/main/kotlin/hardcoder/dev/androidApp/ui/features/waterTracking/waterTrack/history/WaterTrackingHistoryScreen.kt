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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.datetime.getEndOfDay
import hardcoder.dev.datetime.getStartOfDay
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingItem
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingHistoryViewModel
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.calendar.CustomMonthHeader
import hardcoder.dev.uikit.text.Description
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import hardcoder.dev.androidApp.ui.features.waterTracking.waterTrack.WaterTrackItem as WaterTrackItemRow

@Composable
fun WaterTrackingHistoryScreen(
    onGoBack: () -> Unit,
    onTrackUpdate: (WaterTrackingItem) -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.getWaterTrackingHistoryViewModel()
    }
    val state = viewModel.state.collectAsState()

    ScaffoldWrapper(
        content = {
            WaterTrackingContent(
                state = state.value,
                onTrackUpdate = onTrackUpdate,
                onFetchWaterTracks = {
                    viewModel.selectRange(it.getStartOfDay()..it.getEndOfDay())
                }
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
    state: WaterTrackingHistoryViewModel.State,
    onFetchWaterTracks: (LocalDate) -> Unit,
    onTrackUpdate: (WaterTrackingItem) -> Unit
) {
    val calendarState = rememberSelectableCalendarState(initialSelectionMode = SelectionMode.Single)

    LaunchedEffect(key1 = calendarState.selectionState.selection) {
        if (calendarState.selectionState.selection.isNotEmpty()) {
            onFetchWaterTracks(calendarState.selectionState.selection.first().toKotlinLocalDate())
        } else {
            onFetchWaterTracks(LocalDate.now())
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
            waterTrackingItems = state.waterTrackingItems,
            onTrackUpdate = onTrackUpdate
        )
    }
}

@Composable
private fun WaterTracksHistory(
    waterTrackingItems: List<WaterTrackingItem>,
    onTrackUpdate: (WaterTrackingItem) -> Unit
) {
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

