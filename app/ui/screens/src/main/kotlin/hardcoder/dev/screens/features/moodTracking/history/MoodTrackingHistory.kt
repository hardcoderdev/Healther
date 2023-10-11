package hardcoder.dev.screens.features.moodTracking.history

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.coroutines.DefaultBackgroundBackgroundCoroutineDispatchers
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.getEndOfDay
import hardcoder.dev.datetime.getStartOfDay
import hardcoder.dev.entities.features.moodTracking.MoodWithActivities
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.date.MockDateProvider
import hardcoder.dev.mock.dataProviders.features.MoodTrackingMockDataProvider
import hardcoder.dev.screens.features.moodTracking.MoodTrackItem
import hardcoder.dev.uikit.components.calendar.SingleSelectionCalendar
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.ui.resources.R
import kotlinx.datetime.Instant

@Composable
fun MoodTrackingHistory(
    dateTimeProvider: DateTimeProvider,
    dateTimeFormatter: DateTimeFormatter,
    moodWithActivitiesLoadingController: LoadingController<List<MoodWithActivities>>,
    dateRangeInputController: InputController<ClosedRange<Instant>>,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            MoodTrackingHistoryContent(
                dateTimeProvider = dateTimeProvider,
                dateTimeFormatter = dateTimeFormatter,
                dateRangeInputController = dateRangeInputController,
                moodWithActivitiesLoadingController = moodWithActivitiesLoadingController,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.history_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun MoodTrackingHistoryContent(
    dateTimeProvider: DateTimeProvider,
    dateTimeFormatter: DateTimeFormatter,
    moodWithActivitiesLoadingController: LoadingController<List<MoodWithActivities>>,
    dateRangeInputController: InputController<ClosedRange<Instant>>,
) {
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
            dateTimeFormatter = dateTimeFormatter,
            moodWithActivitiesLoadingController = moodWithActivitiesLoadingController,
        )
    }
}

@Composable
private fun MoodTracksHistory(
    dateTimeFormatter: DateTimeFormatter,
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
                            dateTimeFormatter = dateTimeFormatter,
                            moodTrack = moodWithActivityTrack.moodTrack,
                            activitiesList = moodWithActivityTrack.moodActivityList,
                            onUpdate = {
                                /* no-op because money for track has already been collected */
                            },
                        )
                    }
                }
            } else {
                Description(text = stringResource(id = R.string.history_emptyDayHistory_text))
            }
        },
    )
}

@HealtherScreenPhonePreviews
@Composable
private fun MoodTrackingHistoryPreview() {
    HealtherTheme {
        MoodTrackingHistory(
            onGoBack = {},
            dateTimeFormatter = DateTimeFormatter(LocalContext.current),
            dateTimeProvider = DateTimeProvider(dispatchers = DefaultBackgroundBackgroundCoroutineDispatchers),
            dateRangeInputController = MockControllersProvider.inputController(MockDateProvider.instantRange()),
            moodWithActivitiesLoadingController = MockControllersProvider.loadingController(
                data = MoodTrackingMockDataProvider.moodWithActivitiesList(
                    context = LocalContext.current,
                ),
            ),
        )
    }
}