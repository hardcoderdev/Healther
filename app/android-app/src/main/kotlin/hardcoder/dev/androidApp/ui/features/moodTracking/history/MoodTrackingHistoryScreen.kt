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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.androidApp.ui.features.moodTracking.MoodTrackItem
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.datetime.createRangeForThisDay
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrack
import hardcoder.dev.logic.features.moodTracking.moodWithActivity.MoodWithActivities
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

@Composable
fun MoodTrackingHistoryScreen(
    onGoBack: () -> Unit,
    onTrackUpdate: (MoodTrack) -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel { presentationModule.getMoodTrackingHistoryViewModel() }

    ScaffoldWrapper(
        content = {
            MoodTrackingHistoryContent(
                onTrackUpdate = onTrackUpdate,
                dateRangeInputController = viewModel.dateRangeInputController,
                moodWithActivitiesLoadingController = viewModel.moodWithActivityLoadingController
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
    moodWithActivitiesLoadingController: LoadingController<List<MoodWithActivities>>,
    dateRangeInputController: InputController<ClosedRange<Instant>>,
    onTrackUpdate: (MoodTrack) -> Unit
) {
    val calendarState = rememberSelectableCalendarState(initialSelectionMode = SelectionMode.Single)

    LaunchedEffect(key1 = calendarState.selectionState.selection) {
        val date = calendarState.selectionState.selection
            .firstOrNull()?.toKotlinLocalDate() ?: LocalDate.now()
        dateRangeInputController.changeInput(date.createRangeForThisDay())
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
            moodWithActivitiesLoadingController = moodWithActivitiesLoadingController,
            onTrackUpdate = onTrackUpdate
        )
    }
}

@Composable
private fun MoodTracksHistory(
    moodWithActivitiesLoadingController: LoadingController<List<MoodWithActivities>>,
    onTrackUpdate: (MoodTrack) -> Unit
) {
    LoadingContainer(
        controller = moodWithActivitiesLoadingController,
        loadedContent = { moodWithActivitiesList ->
            if (moodWithActivitiesList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(moodWithActivitiesList) { moodWithActivityTrack ->
                        MoodTrackItem(
                            moodTrack = moodWithActivityTrack.moodTrack,
                            onUpdate = onTrackUpdate,
                            activitiesList = moodWithActivityTrack.activityList
                        )
                    }
                }
            } else {
                Description(text = stringResource(id = R.string.moodTracking_history_emptyDay_text))
            }
        }
    )
}