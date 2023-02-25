package hardcoder.dev.android_ui.features.pedometer.history

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.android_ui.LocalPresentationModule
import hardcoder.dev.extensions.getEndOfDay
import hardcoder.dev.extensions.getStartOfDay
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.pedometer.PedometerHistoryViewModel
import hardcoder.dev.presentation.pedometer.PedometerTrackItem
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate

@Composable
fun PedometerHistoryScreen(onGoBack: () -> Unit) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.createPedometerHistoryViewModel()
    }
    val state = viewModel.state.collectAsState()

    ScaffoldWrapper(
        content = {
            PedometerHistoryContent(
                state = state.value,
                onTrackDelete = viewModel::deleteTrack,
                onFetchPedometerTracks = {
                    viewModel.selectRange(it.getStartOfDay()..it.getEndOfDay())
                },
                onTrackUpdate = {

                }
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.featureHistory_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun PedometerHistoryContent(
    state: PedometerHistoryViewModel.State,
    onTrackDelete: (Int) -> Unit,
    onTrackUpdate: (PedometerTrackItem) -> Unit,
    onFetchPedometerTracks: (LocalDate) -> Unit
) {
    val calendarState = rememberSelectableCalendarState(initialSelectionMode = SelectionMode.Single)

    LaunchedEffect(key1 = calendarState.selectionState.selection) {
        if (calendarState.selectionState.selection.isNotEmpty()) {
            onFetchPedometerTracks(
                calendarState.selectionState.selection.first().toKotlinLocalDate()
            )
        } else {
            onFetchPedometerTracks(LocalDate.now())
        }
    }

    Column(Modifier.padding(16.dp)) {
        SelectableCalendar(calendarState = calendarState)
        Spacer(modifier = Modifier.height(16.dp))
        PedometerTracksHistory(
            pedometerTrackItems = state.pedometerTrackItems,
            onTrackUpdate = onTrackUpdate,
            onTrackDelete = onTrackDelete
        )
    }
}

@Composable
private fun PedometerTracksHistory(
    pedometerTrackItems: List<PedometerTrackItem>,
    onTrackDelete: (Int) -> Unit,
    onTrackUpdate: (PedometerTrackItem) -> Unit
) {
    if (pedometerTrackItems.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(pedometerTrackItems) { track ->
                PedometerTrackItem(
                    pedometerTrackItem = track,
                    onDelete = onTrackDelete,
                    onUpdate = onTrackUpdate
                )
            }
        }
    } else {
        Text(
            text = stringResource(id = R.string.featureHistory_emptyDayHistory_text),
            style = MaterialTheme.typography.titleMedium
        )
    }
}