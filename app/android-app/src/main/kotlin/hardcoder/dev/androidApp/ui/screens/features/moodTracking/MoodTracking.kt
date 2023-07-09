package hardcoder.dev.androidApp.ui.screens.features.moodTracking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.screens.features.moodTracking.statistic.MoodTrackingStatisticResolver
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrack
import hardcoder.dev.logic.features.moodTracking.moodWithActivity.MoodWithActivities
import hardcoder.dev.logic.features.moodTracking.statistic.MoodTrackingStatistic
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingViewModel
import hardcoder.dev.uikit.components.chart.ActivityColumnChart
import hardcoder.dev.uikit.components.chart.MINIMUM_ENTRIES_FOR_SHOWING_CHART
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.section.EmptyBlock
import hardcoder.dev.uikit.components.section.EmptySection
import hardcoder.dev.uikit.components.statistic.Statistics
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.components.topBar.Action
import hardcoder.dev.uikit.components.topBar.ActionConfig
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoderdev.healther.app.android.app.R
import kotlin.math.roundToInt
import org.koin.compose.koinInject

@Composable
fun MoodTracking(
    viewModel: MoodTrackingViewModel,
    onCreateMoodTrack: () -> Unit,
    onUpdateMoodTrack: (Int) -> Unit,
    onGoToHistory: () -> Unit,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        onFabClick = onCreateMoodTrack,
        content = {
            MoodTrackingContent(
                moodWithActivitiesController = viewModel.moodWithActivityLoadingController,
                statisticLoadingController = viewModel.statisticLoadingController,
                chartEntriesLoadingController = viewModel.chartEntriesLoadingController,
                onUpdateMoodTrack = onUpdateMoodTrack,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.moodTracking_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
        actionConfig = ActionConfig(
            actions = listOf(
                Action(
                    iconResId = R.drawable.ic_history,
                    onActionClick = onGoToHistory,
                ),
            ),
        ),
    )
}

@Composable
private fun MoodTrackingContent(
    statisticLoadingController: LoadingController<MoodTrackingStatistic?>,
    chartEntriesLoadingController: LoadingController<List<Pair<Int, Int>>>,
    moodWithActivitiesController: LoadingController<List<MoodWithActivities>>,
    onUpdateMoodTrack: (Int) -> Unit,
) {
    LoadingContainer(
        controller1 = statisticLoadingController,
        controller2 = chartEntriesLoadingController,
        controller3 = moodWithActivitiesController,
    ) { statistic, chartEntries, moodWithActivitiesList ->
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            if (chartEntries.isNotEmpty() && statistic != null && moodWithActivitiesList.isNotEmpty()) {
                MoodTrackingStatisticSection(statistic = statistic)
                Spacer(modifier = Modifier.height(32.dp))
                MoodTrackingChartSection(chartEntries)
                Spacer(modifier = Modifier.height(32.dp))
                MoodTrackingDiarySection(
                    moodWithActivitiesList = moodWithActivitiesList,
                    onUpdateTrack = { moodTrack ->
                        onUpdateMoodTrack(moodTrack.id)
                    },
                )
            } else if (moodWithActivitiesList.isEmpty()) {
                EmptySection(emptyTitleResId = R.string.moodTracking_nowEmpty_text)
            } else {
                EmptyBlock(
                    modifier = Modifier.width(300.dp),
                    emptyTitleResId = R.string.moodTracking_diaryNowEmpty_text,
                    lottieAnimationResId = R.raw.empty_astronaut,
                )
            }
        }
    }
}

@Composable
private fun MoodTrackingStatisticSection(statistic: MoodTrackingStatistic) {
    val moodTrackingStatisticResolver = koinInject<MoodTrackingStatisticResolver>()

    Title(text = stringResource(id = R.string.moodTracking_statistic_text))
    Spacer(modifier = Modifier.height(16.dp))
    Statistics(statistics = moodTrackingStatisticResolver.resolve(statistic))
}

@Composable
private fun MoodTrackingChartSection(chartEntries: List<Pair<Int, Int>>) {
    Title(text = stringResource(id = R.string.moodTracking_activity_chart))
    Spacer(modifier = Modifier.height(16.dp))
    if (chartEntries.count() >= MINIMUM_ENTRIES_FOR_SHOWING_CHART) {
        ActivityColumnChart(
            isZoomEnabled = true,
            modifier = Modifier.height(100.dp),
            chartEntries = chartEntries,
            xAxisValueFormatter = { value, _ ->
                value.roundToInt().toString()
            },
            yAxisValueFormatter = { value, _ ->
                "${value.roundToInt()}%"
            },
        )
    } else {
        Description(text = stringResource(id = R.string.moodTracking_chartNotEnoughData_text))
    }
}

@Composable
private fun ColumnScope.MoodTrackingDiarySection(
    moodWithActivitiesList: List<MoodWithActivities>,
    onUpdateTrack: (MoodTrack) -> Unit,
) {
    Title(text = stringResource(id = R.string.moodTracking_diary_text))
    Spacer(modifier = Modifier.height(16.dp))
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .weight(2f),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
    ) {
        items(moodWithActivitiesList) { moodWithActivityTrack ->
            MoodTrackItem(
                moodTrack = moodWithActivityTrack.moodTrack,
                onUpdate = onUpdateTrack,
                activitiesList = moodWithActivityTrack.moodActivityList,
            )
        }
    }
}