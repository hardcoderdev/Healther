package hardcoder.dev.screens.features.foodTracking.foodTracks.observe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.blocks.components.button.fabButton.FabConfig
import hardcoder.dev.blocks.components.containers.ScaffoldWrapper
import hardcoder.dev.blocks.components.text.Title
import hardcoder.dev.blocks.components.topBar.TopBarConfig
import hardcoder.dev.blocks.components.topBar.TopBarType
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.entities.features.foodTracking.FoodTrack
import hardcoder.dev.formatters.MillisDistanceFormatter
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.features.FoodTrackingMockDataProvider
import hardcoder.dev.screens.features.foodTracking.foodTracks.FastingCard
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.sections.EmptySection
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.ui.resources.R

@Composable
fun FoodTracking(
    millisDistanceFormatter: MillisDistanceFormatter,
    timeSinceLastMealLoadingController: LoadingController<Long?>,
    foodTracksLoadingController: LoadingController<List<FoodTrack>>,
    onCreateFoodTrack: () -> Unit,
    onUpdateFoodTrack: (Int) -> Unit,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            FoodTracksContent(
                millisDistanceFormatter = millisDistanceFormatter,
                onUpdateFoodTrack = onUpdateFoodTrack,
                timeSinceLastMealLoadingController = timeSinceLastMealLoadingController,
                foodTracksLoadingController = foodTracksLoadingController,
            )
        },
        fabConfig = FabConfig.LargeFab(
            onFabClick = onCreateFoodTrack,
            iconResId = R.drawable.ic_create,
        ),
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.foodTracking_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun FoodTracksContent(
    millisDistanceFormatter: MillisDistanceFormatter,
    onUpdateFoodTrack: (Int) -> Unit,
    timeSinceLastMealLoadingController: LoadingController<Long?>,
    foodTracksLoadingController: LoadingController<List<FoodTrack>>,
) {
    LoadingContainer(
        controller1 = foodTracksLoadingController,
        controller2 = timeSinceLastMealLoadingController,
        loadedContent = { foodTracksList, timeSinceLastMeal ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                FastingCard(
                    timeDifferenceSinceLastMeal = timeSinceLastMeal,
                    millisDistanceFormatter = millisDistanceFormatter,
                )
                Spacer(modifier = Modifier.height(32.dp))
                FoodTracksListSection(
                    foodTracksList = foodTracksList,
                    onUpdateFoodTrack = onUpdateFoodTrack,
                )
            }
        },
    )
}

@Composable
private fun FoodTracksListSection(
    foodTracksList: List<FoodTrack>,
    onUpdateFoodTrack: (Int) -> Unit,
) {
    Title(text = stringResource(R.string.fasting_foodTracksList_text))
    Spacer(modifier = Modifier.height(16.dp))
    if (foodTracksList.isNotEmpty()) {
        FoodTracksLoadedContent(
            foodTracksList = foodTracksList,
            onUpdateFoodTrack = onUpdateFoodTrack,
        )
    } else {
        FoodTracksEmptyContent()
    }
}

@Composable
private fun FoodTracksLoadedContent(
    foodTracksList: List<FoodTrack>,
    onUpdateFoodTrack: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        items(foodTracksList) { foodTrack ->
            FoodTrackItem(
                modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                foodTrack = foodTrack,
                onUpdateFoodTrack = {
                    onUpdateFoodTrack(foodTrack.id)
                },
            )
        }
    }
}

@Composable
private fun FoodTracksEmptyContent() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        EmptySection(emptyTitleResId = R.string.foodTracking_foodTypes_nowEmpty_text)
    }
}

@Composable
@HealtherScreenPhonePreviews
private fun FoodTrackingPreview() {
    HealtherTheme {
        FoodTracking(
            onGoBack = {},
            onCreateFoodTrack = {},
            onUpdateFoodTrack = {},
            millisDistanceFormatter = MillisDistanceFormatter(
                defaultAccuracy = MillisDistanceFormatter.Accuracy.DAYS,
                context = LocalContext.current,
            ),
            timeSinceLastMealLoadingController = MockControllersProvider.loadingController(0L),
            foodTracksLoadingController = MockControllersProvider.loadingController(
                data = FoodTrackingMockDataProvider.foodTracksList(),
            ),
        )
    }
}