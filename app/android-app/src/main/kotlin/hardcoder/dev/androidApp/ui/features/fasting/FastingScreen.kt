package hardcoder.dev.androidApp.ui.features.fasting

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.ui.DateTimeFormatter
import hardcoder.dev.androidApp.ui.LocalDateTimeFormatter
import hardcoder.dev.androidApp.ui.LocalFastingPlanResourcesProvider
import hardcoder.dev.androidApp.ui.LocalPresentationModule
import hardcoder.dev.entities.features.fasting.statistic.FastingStatistic
import hardcoder.dev.extensions.safeDiv
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.features.fasting.FastingViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.StatisticData
import hardcoder.dev.uikit.Statistics
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.IconTextButton
import hardcoder.dev.uikit.progressBar.CircularProgressBar
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.Headline
import hardcoder.dev.uikit.text.Title

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
                is FastingViewModel.FastingState.NotStarving -> {
                    NotFastingContent(
                        state = fastingState,
                        onCreateFastingTrack = onCreateTrack
                    )
                }

                is FastingViewModel.FastingState.Starving -> {
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
        actionConfig = if (state.value is FastingViewModel.FastingState.NotStarving) {
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
                    titleResId = R.string.dashboard_featureType_fasting,
                    onGoBack = onGoBack
                )
            )
        }
    )
}

private fun FastingStatistic.toGroupedStatistic(
    context: Context,
    favouritePlanResId: Int?
): List<StatisticData> {
    return listOf(
        StatisticData(
            name = context.getString(R.string.fasting_statistic_max_hours_text),
            value = fastingDurationStatistic?.maximumDurationInHours?.toString()
                ?: context.getString(R.string.fasting_statistic_not_enough_data_text)
        ),
        StatisticData(
            name = context.getString(R.string.fasting_statistic_min_hours_text),
            value = fastingDurationStatistic?.minimumDurationInHours?.toString()
                ?: context.getString(R.string.fasting_statistic_not_enough_data_text)
        ),
        StatisticData(
            name = context.getString(R.string.fasting_statistic_average_hours_text),
            value = fastingDurationStatistic?.averageDurationInHours?.toString()
                ?: context.getString(R.string.fasting_statistic_not_enough_data_text)
        ),
        StatisticData(
            name = context.getString(R.string.fasting_statistic_completion_percentage_text),
            value = percentageCompleted?.toString()
                ?: context.getString(R.string.fasting_statistic_not_enough_data_text)
        ),
        StatisticData(
            name = context.getString(R.string.fasting_statistic_favorite_plan_text),
            value = favouritePlanResId?.let { context.getString(favouritePlanResId) }
                ?: context.getString(R.string.fasting_statistic_not_enough_data_text)
        )
    )
}

@Composable
private fun NotFastingContent(
    state: FastingViewModel.FastingState.NotStarving,
    onCreateFastingTrack: () -> Unit
) {
    val context = LocalContext.current
    val fastingPlanResourcesProvider = LocalFastingPlanResourcesProvider.current

    val statisticData = remember(state.fastingStatistic) {
        state.fastingStatistic.toGroupedStatistic(
            context = context,
            favouritePlanResId = state.fastingStatistic.favouritePlan?.let {
                fastingPlanResourcesProvider.provide(it).nameResId
            }
        )
    }

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
            Title(text = stringResource(id = R.string.fasting_lastFastingTracks_text))
            Spacer(modifier = Modifier.height(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                if (state.lastFastingTracks.isEmpty()) {
                    Description(text = stringResource(id = R.string.fasting_noEnoughDataToShowLastTracks_text))
                } else {
                    state.lastFastingTracks.forEach {
                        FastingItem(fastingTrack = it)
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Title(text = stringResource(id = R.string.fasting_statistic_text))
            Spacer(modifier = Modifier.height(24.dp))
            Statistics(
                modifier = Modifier.fillMaxWidth(),
                statistics = statisticData
            )
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
    state: FastingViewModel.FastingState.Starving,
    onEndFasting: () -> Unit
) {
    val context = LocalContext.current
    val fastingPlanResourcesProvider = LocalFastingPlanResourcesProvider.current
    val dateTimeFormatter = LocalDateTimeFormatter.current
    val formattedDate = dateTimeFormatter.formatDateTime(state.startTimeInMillis)

    val fastingPlanResources = fastingPlanResourcesProvider.provide(state.selectedPlan)

    val statisticData = remember(state.fastingStatistic) {
        state.fastingStatistic.toGroupedStatistic(
            context = context,
            favouritePlanResId = state.fastingStatistic.favouritePlan?.let {
                fastingPlanResourcesProvider.provide(it).nameResId
            }
        )
    }

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
                percentage = state.timeLeftInMillis safeDiv state.durationInMillis,
                innerText = dateTimeFormatter.formatMillisDistance(
                    distanceInMillis = state.timeLeftInMillis,
                    accuracy = DateTimeFormatter.Accuracy.SECONDS
                )
            )
            Spacer(modifier = Modifier.height(64.dp))
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
            Spacer(modifier = Modifier.height(32.dp))
            Title(text = stringResource(id = R.string.fasting_statistic_text))
            Spacer(modifier = Modifier.height(32.dp))
            Statistics(statistics = statisticData)
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
    val lifecycleOwner = LocalLifecycleOwner.current
    val dateTimeFormatter = LocalDateTimeFormatter.current

    val formattedFastingTime = dateTimeFormatter.formatMillisDistance(
        distanceInMillis = state.timeLeftInMillis,
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