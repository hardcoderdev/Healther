package hardcoder.dev.androidApp.ui.screens.features.moodTracking.history

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.screens.features.moodTracking.MoodTrackItem
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.getEndOfDay
import hardcoder.dev.datetime.getStartOfDay
import hardcoder.dev.logic.features.moodTracking.moodWithActivity.MoodWithActivities
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingHistoryViewModel
import hardcoder.dev.uikit.components.calendar.SingleSelectionCalendar
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoderdev.healther.app.android.app.R
import kotlinx.datetime.Instant
import org.koin.compose.koinInject

@Composable
fun MoodTrackingHistory(
    viewModel: MoodTrackingHistoryViewModel,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            MoodTrackingHistoryContent(
                dateRangeInputController = viewModel.dateRangeInputController,
                moodWithActivitiesLoadingController = viewModel.moodWithActivityLoadingController,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.moodTracking_history_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun MoodTrackingHistoryContent(
    moodWithActivitiesLoadingController: LoadingController<List<MoodWithActivities>>,
    dateRangeInputController: InputController<ClosedRange<Instant>>,
) {
    val dateTimeProvider = koinInject<DateTimeProvider>()

    Column(Modifier.padding(16.dp)) {
        SingleSelectionCalendar(
            dateTimeProvider = dateTimeProvider,
            inputChanged = { date ->
                dateRangeInputController.changeInput(
                    date.getStartOfDay()..date.getEndOfDay(),
                )
            },
        )
        Spacer(modifier = Modifier.height(16.dp))
        MoodTracksHistory(
            moodWithActivitiesLoadingController = moodWithActivitiesLoadingController,
        )
    }
}

@Composable
private fun MoodTracksHistory(
    moodWithActivitiesLoadingController: LoadingController<List<MoodWithActivities>>,
) {
    LoadingContainer(
        controller = moodWithActivitiesLoadingController,
        loadedContent = { moodWithActivitiesList ->
            if (moodWithActivitiesList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                ) {
                    items(moodWithActivitiesList) { moodWithActivityTrack ->
                        MoodTrackItem(
                            moodTrack = moodWithActivityTrack.moodTrack,
                            activitiesList = moodWithActivityTrack.moodActivityList,
                            onUpdate = {
                                /* no-op because money for track has already been collected */
                            }
                        )
                    }
                }
            } else {
                Description(text = stringResource(id = R.string.moodTracking_history_emptyDay_text))
            }
        },
    )
}