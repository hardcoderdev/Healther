package hardcoder.dev.androidApp.ui.screens.features.fasting.history

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
import hardcoder.dev.androidApp.ui.formatters.DateTimeFormatter
import hardcoder.dev.androidApp.ui.formatters.MillisDistanceFormatter
import hardcoder.dev.androidApp.ui.screens.features.fasting.FastingItem
import hardcoder.dev.androidApp.ui.screens.features.fasting.plans.FastingPlanResourcesProvider
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.coroutines.DefaultBackgroundBackgroundCoroutineDispatchers
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.getEndOfDay
import hardcoder.dev.datetime.getStartOfDay
import hardcoder.dev.logic.features.fasting.track.FastingTrack
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.date.MockDateProvider
import hardcoder.dev.mock.dataProviders.features.FastingMockDataProvider
import hardcoder.dev.uikit.components.calendar.SingleSelectionCalendar
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.resources.R
import kotlinx.datetime.Instant

@Composable
fun FastingHistory(
    dateTimeProvider: DateTimeProvider,
    millisDistanceFormatter: MillisDistanceFormatter,
    dateTimeFormatter: DateTimeFormatter,
    fastingPlanResourcesProvider: FastingPlanResourcesProvider,
    dateRangeInputController: InputController<ClosedRange<Instant>>,
    fastingTracksLoadingController: LoadingController<List<FastingTrack>>,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            FastingHistoryContent(
                dateRangeInputController = dateRangeInputController,
                millisDistanceFormatter = millisDistanceFormatter,
                dateTimeFormatter = dateTimeFormatter,
                dateTimeProvider = dateTimeProvider,
                fastingPlanResourcesProvider = fastingPlanResourcesProvider,
                fastingTracksLoadingController = fastingTracksLoadingController,
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
private fun FastingHistoryContent(
    millisDistanceFormatter: MillisDistanceFormatter,
    dateTimeFormatter: DateTimeFormatter,
    dateTimeProvider: DateTimeProvider,
    fastingPlanResourcesProvider: FastingPlanResourcesProvider,
    dateRangeInputController: InputController<ClosedRange<Instant>>,
    fastingTracksLoadingController: LoadingController<List<FastingTrack>>,
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
        FastingTracksHistory(
            millisDistanceFormatter = millisDistanceFormatter,
            dateTimeFormatter = dateTimeFormatter,
            fastingPlanResourcesProvider = fastingPlanResourcesProvider,
            fastingTracksLoadingController = fastingTracksLoadingController,
        )
    }
}

@Composable
private fun FastingTracksHistory(
    millisDistanceFormatter: MillisDistanceFormatter,
    dateTimeFormatter: DateTimeFormatter,
    fastingPlanResourcesProvider: FastingPlanResourcesProvider,
    fastingTracksLoadingController: LoadingController<List<FastingTrack>>,
) {
    LoadingContainer(
        controller = fastingTracksLoadingController,
        loadedContent = { fastingTracksList ->
            Spacer(modifier = Modifier.height(16.dp))
            if (fastingTracksList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                ) {
                    items(fastingTracksList) { fastingTrack ->
                        FastingItem(
                            millisDistanceFormatter = millisDistanceFormatter,
                            dateTimeFormatter = dateTimeFormatter,
                            fastingPlanResourcesProvider = fastingPlanResourcesProvider,
                            fastingTrack = fastingTrack,
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                Description(text = stringResource(id = R.string.history_emptyDayHistory_text))
            }
        },
    )
}

@HealtherScreenPhonePreviews
@Composable
private fun FastingHistoryPreview() {
    HealtherTheme {
        FastingHistory(
            onGoBack = {},
            dateTimeFormatter = DateTimeFormatter(context = LocalContext.current),
            dateTimeProvider = DateTimeProvider(dispatchers = DefaultBackgroundBackgroundCoroutineDispatchers),
            millisDistanceFormatter = MillisDistanceFormatter(
                context = LocalContext.current,
                defaultAccuracy = MillisDistanceFormatter.Accuracy.DAYS,
            ),
            fastingPlanResourcesProvider = FastingPlanResourcesProvider(),
            dateRangeInputController = MockControllersProvider.inputController(MockDateProvider.instantRange()),
            fastingTracksLoadingController = MockControllersProvider.loadingController(
                data = FastingMockDataProvider.fastingTracksList(),
            ),
        )
    }
}