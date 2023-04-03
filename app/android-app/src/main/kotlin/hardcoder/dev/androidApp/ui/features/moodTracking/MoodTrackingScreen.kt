package hardcoder.dev.androidApp.ui.features.moodTracking

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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.androidApp.di.LocalUIModule
import hardcoder.dev.healther.R
import hardcoder.dev.logic.entities.features.moodTracking.MoodTrack
import hardcoder.dev.logic.entities.features.moodTracking.statistic.MoodTrackingStatistic
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.Statistics
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.charts.ActivityColumnChart
import hardcoder.dev.uikit.charts.MINIMUM_ENTRIES_FOR_SHOWING_CHART
import hardcoder.dev.uikit.sections.EmptyBlock
import hardcoder.dev.uikit.sections.EmptySection
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.Title
import kotlin.math.roundToInt

@Composable
fun MoodTrackingScreen(
    onGoBack: () -> Unit,
    onCreateMoodTrack: () -> Unit,
    onUpdate: (MoodTrack) -> Unit,
    onGoToHistory: () -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.getMoodTrackingViewModel()
    }
    val state = viewModel.state.collectAsState()

    ScaffoldWrapper(
        onFabClick = onCreateMoodTrack,
        content = {
            MoodContent(
                state = state.value,
                onUpdateTrack = onUpdate
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.moodTracking_title_topBar,
                onGoBack = onGoBack
            )
        ),
        actionConfig = ActionConfig(
            actions = listOf(
                Action(
                    iconResId = R.drawable.ic_history,
                    onActionClick = onGoToHistory
                )
            )
        )
    )
}

@Composable
fun MoodContent(
    state: MoodTrackingViewModel.State,
    onUpdateTrack: (MoodTrack) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (state.chartEntries.isNotEmpty() && state.moodTrackingStatistic != null && state.moodWithHobbiesList.isNotEmpty()) {
            MoodTrackingStatisticSection(statistic = requireNotNull(state.moodTrackingStatistic))
            Spacer(modifier = Modifier.height(32.dp))
            MoodTrackingChartSection(state = state)
            Spacer(modifier = Modifier.height(32.dp))
            MoodTrackingDiarySection(state = state, onUpdateTrack = onUpdateTrack)
        } else if (state.moodWithHobbiesList.isEmpty()) {
            EmptySection(emptyTitleResId = R.string.moodTracking_nowEmpty_text)
        } else {
            EmptyBlock(
                modifier = Modifier.width(300.dp),
                emptyTitleResId = R.string.moodTracking_diaryNowEmpty_text,
                lottieAnimationResId = R.raw.empty_astronaut
            )
        }
    }
}

@Composable
private fun MoodTrackingStatisticSection(statistic: MoodTrackingStatistic) {
    val uiModule = LocalUIModule.current
    val moodTrackingStatisticResolver = uiModule.moodTrackingStatisticResolver

    Title(text = stringResource(id = R.string.moodTracking_statistic_text))
    Spacer(modifier = Modifier.height(16.dp))
    Statistics(statistics = moodTrackingStatisticResolver.resolve(statistic))
}

@Composable
private fun MoodTrackingChartSection(state: MoodTrackingViewModel.State) {
    Title(text = stringResource(id = R.string.moodTracking_activity_chart))
    Spacer(modifier = Modifier.height(16.dp))
    if (state.chartEntries.count() >= MINIMUM_ENTRIES_FOR_SHOWING_CHART) {
        ActivityColumnChart(
            isZoomEnabled = true,
            modifier = Modifier.height(100.dp),
            chartEntries = state.chartEntries,
            xAxisValueFormatter = { value, _ ->
                value.roundToInt().toString()
            },
            yAxisValueFormatter = { value, _ ->
                "${value.roundToInt()}%"
            }
        )
    } else {
        Description(text = stringResource(id = R.string.moodTracking_chartNotEnoughData_text))
    }
}

@Composable
private fun ColumnScope.MoodTrackingDiarySection(
    state: MoodTrackingViewModel.State,
    onUpdateTrack: (MoodTrack) -> Unit
) {
    Title(text = stringResource(id = R.string.moodTracking_diary_text))
    Spacer(modifier = Modifier.height(16.dp))
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .weight(2f),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(state.moodWithHobbiesList) { track ->
            MoodTrackItem(
                moodTrack = track.moodTrack,
                onUpdate = onUpdateTrack,
                hobbyList = track.hobbyList
            )
        }
    }
}