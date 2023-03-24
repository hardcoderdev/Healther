package hardcoder.dev.androidApp.ui.features.starvation.history

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
import hardcoder.dev.androidApp.ui.LocalPresentationModule
import hardcoder.dev.androidApp.ui.features.starvation.StarvationItem
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.features.starvation.StarvationHistoryViewModel
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.text.Title
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate

@Composable
fun StarvationHistoryScreen(onGoBack: () -> Unit) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.createStarvationHistoryViewModel()
    }
    val state = viewModel.state.collectAsState()

    ScaffoldWrapper(
        content = {
            StarvationHistoryContent(
                state = state.value,
                onFetchStarvationTracks = viewModel::selectDay
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.starvationHistory_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun StarvationHistoryContent(
    state: StarvationHistoryViewModel.State,
    onFetchStarvationTracks: (LocalDate) -> Unit
) {
    val calendarState = rememberSelectableCalendarState(initialSelectionMode = SelectionMode.Single)

    LaunchedEffect(key1 = calendarState.selectionState.selection) {
        if (calendarState.selectionState.selection.isNotEmpty()) {
            onFetchStarvationTracks(
                calendarState.selectionState.selection.first().toKotlinLocalDate()
            )
        } else {
            onFetchStarvationTracks(LocalDate.now())
        }
    }

    Column(Modifier.padding(16.dp)) {
        SelectableCalendar(calendarState = calendarState)
        Spacer(modifier = Modifier.height(16.dp))
        StarvationTracksHistory(state = state)
    }
}

@Composable
private fun StarvationTracksHistory(state: StarvationHistoryViewModel.State) {
    Spacer(modifier = Modifier.height(16.dp))
    if (state.starvationTracks.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(state.starvationTracks) { track ->
                StarvationItem(starvationTrack = track)
            }
        }
    } else {
        Spacer(modifier = Modifier.height(16.dp))
        Title(text = stringResource(id = R.string.starvationHistory_title_topBar))
    }
}