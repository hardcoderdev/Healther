package hardcoder.dev.uikit.components.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.calendar.compose.datepicker.EpicDatePicker
import epicarchitect.calendar.compose.datepicker.config.rememberEpicDatePickerConfig
import epicarchitect.calendar.compose.datepicker.state.EpicDatePickerState
import epicarchitect.calendar.compose.datepicker.state.rememberEpicDatePickerState
import epicarchitect.calendar.compose.pager.config.rememberEpicCalendarPagerConfig
import hardcoder.dev.coroutines.DefaultBackgroundBackgroundCoroutineDispatchers
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.uikit.preview.UiKitPhonePreview
import hardcoder.dev.uikit.values.HealtherThemePreview
import kotlinx.datetime.LocalDate

@Composable
fun SingleSelectionCalendar(
    inputChanged: (LocalDate) -> Unit,
    dateTimeProvider: DateTimeProvider,
) {
    val calendarState = rememberEpicDatePickerState(
        config = rememberEpicDatePickerConfig(
            pagerConfig = rememberEpicCalendarPagerConfig(),
            selectionContentColor = MaterialTheme.colorScheme.onPrimary,
            selectionContainerColor = MaterialTheme.colorScheme.primary,
        ),
        selectionMode = EpicDatePickerState.SelectionMode.Single(),
        selectedDates = listOf(dateTimeProvider.currentDate()),
    )

    LaunchedEffect(key1 = calendarState.selectedDates) {
        if (calendarState.selectedDates.isNotEmpty()) {
            val date = calendarState.selectedDates.first()
            inputChanged(date)
        } else {
            inputChanged(dateTimeProvider.currentDate())
        }
    }

    Column {
        CustomMonthHeader(
            state = calendarState,
            month = calendarState.pagerState.currentMonth,
        )
        Spacer(modifier = Modifier.height(16.dp))
        EpicDatePicker(state = calendarState)
    }
}

@UiKitPhonePreview
@Composable
private fun SingleSelectionCalendarWithHeaderPreview() {
    HealtherThemePreview {
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            SingleSelectionCalendar(
                inputChanged = {},
                dateTimeProvider = DateTimeProvider(DefaultBackgroundBackgroundCoroutineDispatchers),
            )
        }
    }
}