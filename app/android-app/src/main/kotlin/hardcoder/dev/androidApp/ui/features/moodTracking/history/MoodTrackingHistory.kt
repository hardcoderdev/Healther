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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import epicarchitect.calendar.compose.basis.config.rememberBasisEpicCalendarConfig
import epicarchitect.calendar.compose.datepicker.EpicDatePicker
import epicarchitect.calendar.compose.datepicker.config.rememberEpicDatePickerConfig
import epicarchitect.calendar.compose.datepicker.state.EpicDatePickerState
import epicarchitect.calendar.compose.datepicker.state.rememberEpicDatePickerState
import epicarchitect.calendar.compose.pager.config.rememberEpicCalendarPagerConfig
import hardcoder.dev.androidApp.ui.features.moodTracking.MoodTrackItem
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.datetime.createRangeForCurrentDay
import hardcoder.dev.datetime.getEndOfDay
import hardcoder.dev.datetime.getStartOfDay
import hardcoder.dev.datetime.currentDate
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrack
import hardcoder.dev.logic.features.moodTracking.moodWithActivity.MoodWithActivities
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingHistoryViewModel
import hardcoder.dev.uikit.LoadingContainer
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.calendar.CustomMonthHeader
import hardcoder.dev.uikit.text.Description
import hardcoderdev.healther.app.android.app.R
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoodTrackingHistory(
    onGoBack: () -> Unit,
    onMoodTrackingUpdate: (Int) -> Unit
) {
    val viewModel = koinViewModel<MoodTrackingHistoryViewModel>()

    ScaffoldWrapper(
        content = {
            MoodTrackingHistoryContent(
                onTrackUpdate = onMoodTrackingUpdate,
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
    onTrackUpdate: (Int) -> Unit
) {
    val calendarState = rememberEpicDatePickerState(
        config = rememberEpicDatePickerConfig(
            pagerConfig = rememberEpicCalendarPagerConfig(basisConfig = rememberBasisEpicCalendarConfig(),),
            selectionContentColor = MaterialTheme.colorScheme.onPrimary,
            selectionContainerColor = MaterialTheme.colorScheme.primary,
        ),
        selectionMode = EpicDatePickerState.SelectionMode.Single(),
        selectedDates = listOf(LocalDate.currentDate())
    )

    LaunchedEffect(key1 = calendarState.selectedDates) {
        if (calendarState.selectedDates.isNotEmpty()) {
            val date = calendarState.selectedDates.first()
            dateRangeInputController.changeInput(date.getStartOfDay()..date.getEndOfDay())
        } else {
            dateRangeInputController.changeInput(LocalDate.createRangeForCurrentDay())
        }
    }

    Column(Modifier.padding(16.dp)) {
        CustomMonthHeader(
            state = calendarState,
            month = calendarState.pagerState.currentMonth
        )
        Spacer(modifier = Modifier.height(16.dp))
        EpicDatePicker(state = calendarState)
        Spacer(modifier = Modifier.height(16.dp))
        MoodTracksHistory(
            moodWithActivitiesLoadingController = moodWithActivitiesLoadingController,
            onTrackUpdate = { moodTrack ->
                onTrackUpdate(moodTrack.id)
            }
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
                            activitiesList = moodWithActivityTrack.moodActivityList
                        )
                    }
                }
            } else {
                Description(text = stringResource(id = R.string.moodTracking_history_emptyDay_text))
            }
        }
    )
}