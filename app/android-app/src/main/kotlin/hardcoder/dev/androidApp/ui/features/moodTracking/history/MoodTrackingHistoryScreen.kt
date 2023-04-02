package hardcoder.dev.androidApp.ui.features.moodTracking.history

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
import hardcoder.dev.androidApp.ui.features.moodTracking.MoodTrackItem
import hardcoder.dev.entities.features.moodTracking.MoodTrack
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.features.moodTracking.MoodTrackWithHobbies
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingHistoryViewModel
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
fun MoodTrackingHistoryScreen(
    onGoBack: () -> Unit,
    onTrackUpdate: (MoodTrack) -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.getMoodTrackingHistoryViewModel()
    }
    val state = viewModel.state.collectAsState()

    ScaffoldWrapper(
        content = {
            MoodTrackingHistoryContent(
                state = state.value,
                onFetchMoodTracks = viewModel::updateSelectedRange,
                onTrackDelete = viewModel::deleteTrack,
                onTrackUpdate = onTrackUpdate
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.moodTracking_history_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun MoodTrackingHistoryContent(
    state: MoodTrackingHistoryViewModel.State,
    onFetchMoodTracks: (LocalDate) -> Unit,
    onTrackUpdate: (MoodTrack) -> Unit,
    onTrackDelete: (Int) -> Unit
) {
    val calendarState = rememberSelectableCalendarState(initialSelectionMode = SelectionMode.Single)

    LaunchedEffect(key1 = calendarState.selectionState.selection) {
        if (calendarState.selectionState.selection.isNotEmpty()) {
            onFetchMoodTracks(calendarState.selectionState.selection.first().toKotlinLocalDate())
        } else {
            onFetchMoodTracks(LocalDate.now())
        }
    }

    Column(Modifier.padding(16.dp)) {
        SelectableCalendar(
            calendarState = calendarState,
            monthHeader = {
                CustomMonthHeader(monthState = it)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        MoodTracksHistory(
            moodTrackList = state.moodTrackWithHobbyList,
            onTrackUpdate = onTrackUpdate,
            onTrackDelete = onTrackDelete
        )
    }
}

@Composable
private fun MoodTracksHistory(
    moodTrackList: List<MoodTrackWithHobbies>,
    onTrackDelete: (Int) -> Unit,
    onTrackUpdate: (MoodTrack) -> Unit
) {
    if (moodTrackList.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(moodTrackList) { track ->
                MoodTrackItem(
                    moodTrack = track.moodTrack,
                    onUpdate = onTrackUpdate,
                    hobbyTrackList = track.hobbyTrackList
                )
            }
        }
    } else {
        Description(text = stringResource(id = R.string.moodTracking_history_emptyDay_text))
    }
}