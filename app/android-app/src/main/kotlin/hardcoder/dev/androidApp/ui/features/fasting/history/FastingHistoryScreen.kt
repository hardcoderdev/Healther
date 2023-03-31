package hardcoder.dev.androidApp.ui.features.fasting.history

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
import hardcoder.dev.androidApp.ui.features.fasting.FastingItem
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.features.fasting.FastingHistoryViewModel
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

@Composable
fun FastingHistoryScreen(onGoBack: () -> Unit) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.getFastingHistoryViewModel()
    }
    val state = viewModel.state.collectAsState()

    ScaffoldWrapper(
        content = {
            FastingHistoryContent(
                state = state.value,
                onFetchFastingTracks = viewModel::selectDay
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.fastingHistory_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun FastingHistoryContent(
    state: FastingHistoryViewModel.State,
    onFetchFastingTracks: (LocalDate) -> Unit
) {
    val calendarState = rememberSelectableCalendarState(initialSelectionMode = SelectionMode.Single)

    LaunchedEffect(key1 = calendarState.selectionState.selection) {
        if (calendarState.selectionState.selection.isNotEmpty()) {
            onFetchFastingTracks(calendarState.selectionState.selection.first().toKotlinLocalDate())
        } else {
            onFetchFastingTracks(LocalDate.now())
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
        FastingTracksHistory(state = state)
    }
}

@Composable
private fun FastingTracksHistory(state: FastingHistoryViewModel.State) {
    Spacer(modifier = Modifier.height(16.dp))
    if (state.fastingTracks.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(state.fastingTracks) { track ->
                FastingItem(fastingTrack = track)
            }
        }
    } else {
        Spacer(modifier = Modifier.height(16.dp))
        Description(text = stringResource(id = R.string.fastingHistory_emptyDayHistory_text))
    }
}