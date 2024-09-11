package hardcoder.dev.screens.features.waterTracking.waterTrack.history

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
import hardcoder.dev.blocks.components.containers.ScaffoldWrapper
import hardcoder.dev.blocks.components.text.Description
import hardcoder.dev.blocks.components.topBar.TopBarConfig
import hardcoder.dev.blocks.components.topBar.TopBarType
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.coroutines.DefaultBackgroundBackgroundCoroutineDispatchers
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.getEndOfDay
import hardcoder.dev.datetime.getStartOfDay
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.date.MockDateProvider
import hardcoder.dev.mock.dataProviders.features.WaterTrackingMockDataProvider
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingItem
import hardcoder.dev.screens.features.waterTracking.waterTrack.WaterTrackItem
import hardcoder.dev.uikit.components.calendar.SingleSelectionCalendar
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.ui.resources.R
import kotlinx.datetime.Instant

@Composable
fun WaterTrackingHistory(
    dateTimeProvider: DateTimeProvider,
    dateRangeInputController: InputController<ClosedRange<Instant>>,
    waterTracksLoadingController: LoadingController<List<WaterTrackingItem>>,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            WaterTrackingHistoryContent(
                dateRangeInputController = dateRangeInputController,
                itemsLoadingController = waterTracksLoadingController,
                dateTimeProvider = dateTimeProvider,
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
private fun WaterTrackingHistoryContent(
    dateTimeProvider: DateTimeProvider,
    dateRangeInputController: InputController<ClosedRange<Instant>>,
    itemsLoadingController: LoadingController<List<WaterTrackingItem>>,
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
        WaterTracksHistory(
            itemsLoadingController = itemsLoadingController,
        )
    }
}

@Composable
private fun WaterTracksHistory(
    itemsLoadingController: LoadingController<List<WaterTrackingItem>>,
) {
    LoadingContainer(controller = itemsLoadingController) { waterTrackingItems ->
        if (waterTrackingItems.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
            ) {
                items(waterTrackingItems) { track ->
                    WaterTrackItem(
                        waterTrackingItem = track,
                        onUpdate = {},
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.height(16.dp))
            Description(text = stringResource(id = R.string.history_emptyDayHistory_text))
        }
    }
}

@HealtherScreenPhonePreviews
@Composable
private fun WaterTrackingHistoryPreview() {
    HealtherTheme {
        WaterTrackingHistory(
            onGoBack = {},
            dateTimeProvider = DateTimeProvider(dispatchers = DefaultBackgroundBackgroundCoroutineDispatchers),
            dateRangeInputController = MockControllersProvider.inputController(
                input = MockDateProvider.instantRange(),
            ),
            waterTracksLoadingController = MockControllersProvider.loadingController(
                data = WaterTrackingMockDataProvider.waterTrackingItemsList(
                    context = LocalContext.current,
                ),
            ),
        )
    }
}