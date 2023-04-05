package hardcoder.dev.androidApp.ui.features.fasting

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.androidApp.di.LocalUIModule
import hardcoder.dev.androidApp.ui.formatters.DateTimeFormatter
import hardcoder.dev.extensions.safeDiv
import hardcoder.dev.healther.R
import hardcoder.dev.logic.features.fasting.statistic.FastingStatistic
import hardcoder.dev.logic.features.fasting.track.FastingTrack
import hardcoder.dev.presentation.features.fasting.FastingViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.Statistics
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.IconTextButton
import hardcoder.dev.uikit.charts.ActivityColumnChart
import hardcoder.dev.uikit.charts.MINIMUM_ENTRIES_FOR_SHOWING_CHART
import hardcoder.dev.uikit.progressBar.CircularProgressBar
import hardcoder.dev.uikit.sections.EmptyBlock
import hardcoder.dev.uikit.sections.EmptySection
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.Headline
import hardcoder.dev.uikit.text.Title
import kotlin.math.roundToInt

@Composable
fun FastingScreen(
    onGoBack: () -> Unit,
    onHistoryDetails: () -> Unit,
    onCreateTrack: () -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel { presentationModule.getFastingViewModel() }
    val state = viewModel.state.collectAsState()

    ScaffoldWrapper(
        content = {
            when (val fastingState = state.value) {
                is FastingViewModel.FastingState.NotFasting -> {
                    NotFastingContent(
                        state = fastingState,
                        onCreateFastingTrack = onCreateTrack
                    )
                }

                is FastingViewModel.FastingState.Fasting -> {
                    FastingContent(
                        state = fastingState,
                        onEndFasting = viewModel::interruptTrack
                    )
                }

                is FastingViewModel.FastingState.Finished -> {
                    FinishFastingContent(
                        state = fastingState,
                        onClose = viewModel::clearFasting
                    )
                }
            }
        },
        actionConfig = if (state.value is FastingViewModel.FastingState.NotFasting) {
            ActionConfig(
                actions = listOf(
                    Action(
                        iconResId = R.drawable.ic_history,
                        onActionClick = onHistoryDetails
                    )
                )
            )
        } else {
            null
        },
        topBarConfig = if (state.value is FastingViewModel.FastingState.Finished) {
            TopBarConfig(
                type = TopBarType.TitleTopBar(
                    titleResId = R.string.fasting_finish_title_topBar
                )
            )
        } else {
            TopBarConfig(
                type = TopBarType.TopBarWithNavigationBack(
                    titleResId = R.string.fasting_title_topBar,
                    onGoBack = onGoBack
                )
            )
        }
    )
}

@Composable
private fun NotFastingContent(
    state: FastingViewModel.FastingState.NotFasting,
    onCreateFastingTrack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            Modifier
                .weight(2f)
                .verticalScroll(rememberScrollState())
        ) {
            FastingLastTracksSection(lastFastingTrackList = state.lastFastingTracks)
            Spacer(modifier = Modifier.height(32.dp))
            state.fastingStatistic?.let { statistic ->
                FastingStatisticSection(statistic = statistic)
                Spacer(modifier = Modifier.height(32.dp))
                FastingChartSection(fastingChartEntries = state.chartEntries)
            } ?: run {
                EmptySection(emptyTitleResId = R.string.fasting_nowEmpty_text)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        IconTextButton(
            iconResId = R.drawable.ic_play,
            labelResId = R.string.fasting_start_buttonText,
            onClick = onCreateFastingTrack
        )
    }
}

@Composable
private fun FastingContent(
    state: FastingViewModel.FastingState.Fasting,
    onEndFasting: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            Modifier
                .weight(2f)
                .verticalScroll(rememberScrollState())
        ) {
            FastingProgressSection(state = state)
            Spacer(modifier = Modifier.height(64.dp))
            FastingInfoSection(state = state)
            Spacer(modifier = Modifier.height(16.dp))
            state.fastingStatistic?.let { statistic ->
                FastingStatisticSection(statistic = statistic)
            } ?: run {
                EmptyBlock(
                    emptyTitleResId = R.string.fasting_nowEmpty_text,
                    lottieAnimationResId = R.raw.empty_astronaut
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        IconTextButton(
            iconResId = R.drawable.ic_close,
            labelResId = R.string.fasting_interrupt_buttonText,
            onClick = onEndFasting
        )
    }
}

@Composable
private fun FinishFastingContent(
    state: FastingViewModel.FastingState.Finished,
    onClose: () -> Unit
) {
    val uiModule = LocalUIModule.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val dateTimeFormatter = uiModule.dateTimeFormatter

    val formattedFastingTime = dateTimeFormatter.formatMillisDistance(
        distanceInMillis = state.timeLeftInMillis.inWholeMilliseconds,
        usePlurals = true,
        accuracy = if (state.isInterrupted) {
            DateTimeFormatter.Accuracy.MINUTES
        } else {
            DateTimeFormatter.Accuracy.HOURS
        }
    )

    val advicesStringResource = if (state.isInterrupted) {
        stringResource(
            id = R.string.fasting_finish_fail_results_text,
            formatArgs = arrayOf(formattedFastingTime)
        )
    } else {
        stringResource(id = R.string.fasting_finish_success_results_text)
    }

    DisposableEffect(key1 = Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP || event == Lifecycle.Event.ON_DESTROY) {
                onClose()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(Modifier.weight(2f)) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                painter = painterResource(id = R.drawable.fasting_finish_image),
                contentDescription = stringResource(id = R.string.fasting_finish_image_contentDescription)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Headline(text = stringResource(id = R.string.fasting_finish_text))
            Spacer(modifier = Modifier.height(16.dp))
            Description(text = advicesStringResource)
        }
        IconTextButton(
            iconResId = R.drawable.ic_navigate_next,
            labelResId = R.string.fasting_finish_goNext_buttonText,
            onClick = onClose
        )
    }
}

@Composable
private fun ColumnScope.FastingProgressSection(state: FastingViewModel.FastingState.Fasting) {
    val uiModule = LocalUIModule.current
    val dateTimeFormatter = uiModule.dateTimeFormatter

    Headline(
        text = stringResource(id = R.string.fasting_in_progress_text),
        modifier = Modifier.align(CenterHorizontally)
    )
    Spacer(modifier = Modifier.height(32.dp))
    CircularProgressBar(
        modifier = Modifier.align(CenterHorizontally),
        radius = 100.dp,
        fontSize = 24.sp,
        strokeWidth = 12.dp,
        percentage = state.timeLeftInMillis.inWholeMilliseconds safeDiv state.durationInMillis.inWholeMilliseconds,
        innerText = dateTimeFormatter.formatMillisDistance(
            distanceInMillis = state.timeLeftInMillis.inWholeMilliseconds,
            accuracy = DateTimeFormatter.Accuracy.SECONDS
        )
    )
}

@Composable
private fun FastingInfoSection(state: FastingViewModel.FastingState.Fasting) {
    val uiModule = LocalUIModule.current
    val fastingPlanResourcesProvider = uiModule.fastingPlanResourcesProvider
    val dateTimeFormatter = uiModule.dateTimeFormatter
    val formattedDate = dateTimeFormatter.formatTime(state.startTimeInMillis)
    val fastingPlanResources = fastingPlanResourcesProvider.provide(state.selectedPlan)

    Description(
        text = stringResource(
            id = R.string.fasting_startDate_text,
            formatArgs = arrayOf(formattedDate)
        )
    )
    Spacer(modifier = Modifier.height(12.dp))
    Description(
        text = stringResource(
            id = R.string.fasting_yourSelectedPlan_formatText,
            formatArgs = arrayOf(stringResource(id = fastingPlanResources.nameResId))
        )
    )
}

@Composable
private fun FastingLastTracksSection(lastFastingTrackList: List<FastingTrack>) {
    Title(text = stringResource(id = R.string.fasting_lastFastingTracks_text))
    Spacer(modifier = Modifier.height(16.dp))
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (lastFastingTrackList.isEmpty()) {
            Description(text = stringResource(id = R.string.fasting_lastTracksNotEnoughData_text))
        } else {
            lastFastingTrackList.forEach {
                FastingItem(fastingTrack = it)
            }
        }
    }
}

@Composable
private fun FastingStatisticSection(statistic: FastingStatistic) {
    val uiModule = LocalUIModule.current
    val fastingStatisticResolver = uiModule.fastingStatisticResolver
    val fastingPlanResourcesProvider = uiModule.fastingPlanResourcesProvider

    Title(text = stringResource(id = R.string.fasting_statistic_text))
    Spacer(modifier = Modifier.height(24.dp))
    Statistics(
        modifier = Modifier.fillMaxWidth(),
        statistics = fastingStatisticResolver.resolve(
            statistic,
            statistic.favouritePlan?.let {
                fastingPlanResourcesProvider.provide(it).nameResId
            }
        )
    )
}

@Composable
private fun FastingChartSection(fastingChartEntries: List<Pair<Int, Long>>) {
    Title(text = stringResource(id = R.string.fasting_activity_chart))
    Spacer(modifier = Modifier.height(16.dp))
    if (fastingChartEntries.count() >= MINIMUM_ENTRIES_FOR_SHOWING_CHART) {
        ActivityColumnChart(
            isZoomEnabled = true,
            modifier = Modifier.height(200.dp),
            chartEntries = fastingChartEntries,
            xAxisValueFormatter = { value, _ ->
                value.roundToInt().toString()
            },
            yAxisValueFormatter = { value, _ ->
                value.roundToInt().toString()
            }
        )
    } else {
        Description(text = stringResource(id = R.string.fasting_chartNotEnoughData_text))
    }
}