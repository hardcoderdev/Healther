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
import hardcoder.dev.androidApp.ui.features.fasting.FastingItem
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.datetime.createRangeForCurrentDay
import hardcoder.dev.datetime.getEndOfDay
import hardcoder.dev.datetime.getStartOfDay
import hardcoder.dev.datetime.currentDate
import hardcoder.dev.logic.features.fasting.track.FastingTrack
import hardcoder.dev.presentation.features.fasting.FastingHistoryViewModel
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
fun FastingHistory(onGoBack: () -> Unit) {
    val viewModel = koinViewModel<FastingHistoryViewModel>()

    ScaffoldWrapper(
        content = {
            FastingHistoryContent(
                dateRangeInputController = viewModel.dateRangeInputController,
                fastingTracksLoadingController = viewModel.fastingTracksLoadingController
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.fasting_history_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun FastingHistoryContent(
    dateRangeInputController: InputController<ClosedRange<Instant>>,
    fastingTracksLoadingController: LoadingController<List<FastingTrack>>
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
        FastingTracksHistory(fastingTracksLoadingController = fastingTracksLoadingController)
    }
}

@Composable
private fun FastingTracksHistory(fastingTracksLoadingController: LoadingController<List<FastingTrack>>) {
    LoadingContainer(
        controller = fastingTracksLoadingController,
        loadedContent = { fastingTracksList ->
            Spacer(modifier = Modifier.height(16.dp))
            if (fastingTracksList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(fastingTracksList) { fastingTrack ->
                        FastingItem(fastingTrack = fastingTrack)
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                Description(text = stringResource(id = R.string.fasting_history_emptyDayHistory_text))
            }
        }
    )
}