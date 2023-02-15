package hardcoder.dev.healther.ui.screens.waterTracking.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.healther.R
import hardcoder.dev.healther.ui.base.LocalPresentationModule
import hardcoder.dev.healther.ui.base.composables.ScaffoldWrapper
import hardcoder.dev.healther.ui.base.composables.TopBarConfig
import hardcoder.dev.healther.ui.base.composables.TopBarType
import hardcoder.dev.healther.ui.base.composables.WaterTrackItem
import hardcoder.dev.healther.ui.base.extensions.getEndOfDay
import hardcoder.dev.healther.ui.base.extensions.getStartOfDay
import hardcoder.dev.healther.ui.screens.waterTracking.WaterTrackItem
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate

@Composable
fun WaterTrackingHistoryScreen(
    onGoBack: () -> Unit,
    onTrackUpdate: (WaterTrackItem) -> Unit
) {
    val presentationModule = LocalPresentationModule.current

    val viewModel = viewModel {
        presentationModule.createWaterTrackingHistoryViewModel()
    }

    val state = viewModel.state.collectAsState()

    ScaffoldWrapper(
        content = {
            WaterTrackingContent(
                state = state.value,
                onTrackUpdate = onTrackUpdate,
                onTrackDelete = viewModel::deleteTrack,
                onFetchWaterTracks = {
                    viewModel.selectRange(it.getStartOfDay()..it.getEndOfDay())
                }
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.waterHistory_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun WaterTrackingContent(
    state: WaterTrackingHistoryViewModel.State,
    onFetchWaterTracks: (LocalDate) -> Unit,
    onTrackUpdate: (WaterTrackItem) -> Unit,
    onTrackDelete: (Int) -> Unit
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
        SelectableCalendar(calendarState = calendarState)
        Spacer(modifier = Modifier.height(16.dp))
        WaterTracksHistory(
            waterTrackItems = state.waterTrackItems,
            onTrackUpdate = onTrackUpdate,
            onTrackDelete = onTrackDelete
        )
    }
}

@Composable
private fun WaterTracksHistory(
    waterTrackItems: List<WaterTrackItem>,
    onTrackDelete: (Int) -> Unit,
    onTrackUpdate: (WaterTrackItem) -> Unit
) {
    if (waterTrackItems.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(waterTrackItems) { track ->
                WaterTrackItem(
                    waterTrackItem = track,
                    onDelete = onTrackDelete,
                    onUpdate = onTrackUpdate
                )
            }
        }
    } else {
        Text(
            text = stringResource(id = R.string.waterHistory_emptyDayHistory_text),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview
@Composable
fun WaterTrackingContentPreview() {
    WaterTrackingHistoryScreen(onGoBack = {}, onTrackUpdate = {})
}

