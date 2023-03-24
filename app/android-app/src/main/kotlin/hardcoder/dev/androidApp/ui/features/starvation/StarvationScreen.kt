package hardcoder.dev.androidApp.ui.features.starvation

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
import hardcoder.dev.androidApp.ui.LocalPresentationModule
import hardcoder.dev.androidApp.ui.LocalStarvationPlanResourcesProvider
import hardcoder.dev.entities.features.starvation.statistic.StarvationStatistic
import hardcoder.dev.extensions.safeDiv
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.features.starvation.StarvationViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.CircularProgressBar
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.StatisticData
import hardcoder.dev.uikit.Statistics
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.IconTextButton
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.Headline
import hardcoder.dev.uikit.text.Title

@Composable
fun StarvationScreen(
    onGoBack: () -> Unit,
    onHistoryDetails: () -> Unit,
    onCreateStarvationTrack: () -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel { presentationModule.createStarvationViewModel() }
    val state = viewModel.state.collectAsState()

    ScaffoldWrapper(
        content = {
            when (val starvationState = state.value) {
                is StarvationViewModel.StarvationState.NotStarving -> {
                    NotStarvingContent(
                        state = starvationState,
                        onCreateStarvationTrack = onCreateStarvationTrack
                    )
                }

                is StarvationViewModel.StarvationState.Starving -> {
                    StarvingContent(
                        state = starvationState,
                        onEndStarvation = viewModel::interruptTrack
                    )
                }

                is StarvationViewModel.StarvationState.Finished -> {
                    FinishStarvingContent(
                        state = starvationState,
                        onClose = viewModel::clearStarvation
                    )
                }
            }
        },
        actionConfig = if (state.value is StarvationViewModel.StarvationState.NotStarving) {
            ActionConfig(
                actions = listOf(
                    Action(
                        iconResId = R.drawable.ic_more,
                        onActionClick = onHistoryDetails
                    )
                )
            )
        } else {
            null
        },
        topBarConfig = if (state.value is StarvationViewModel.StarvationState.Finished) {
            TopBarConfig(
                type = TopBarType.TitleTopBar(
                    titleResId = R.string.starvation_finish_title_topBar
                )
            )
        } else {
            TopBarConfig(
                type = TopBarType.TopBarWithNavigationBack(
                    titleResId = R.string.dashboard_featureType_starvation,
                    onGoBack = onGoBack
                )
            )
        }
    )
}

private fun StarvationStatistic.toStarvationStatistic(
    context: Context,
    favouritePlanResId: Int?
): List<StatisticData> {
    return listOf(
        StatisticData(
            name = context.getString(R.string.starvation_statistic_max_hours_text),
            value = starvationDurationStatistic?.maximumDurationInHours?.toString()
                ?: context.getString(R.string.starvation_statistic_not_enough_data_text)
        ),
        StatisticData(
            name = context.getString(R.string.starvation_statistic_min_hours_text),
            value = starvationDurationStatistic?.minimumDurationInHours?.toString()
                ?: context.getString(R.string.starvation_statistic_not_enough_data_text)
        ),
        StatisticData(
            name = context.getString(R.string.starvation_statistic_average_hours_text),
            value = starvationDurationStatistic?.averageDurationInHours?.toString()
                ?: context.getString(R.string.starvation_statistic_not_enough_data_text)
        ),
        StatisticData(
            name = context.getString(R.string.starvation_statistic_completion_percentage_text),
            value = percentageCompleted?.toString()
                ?: context.getString(R.string.starvation_statistic_not_enough_data_text)
        ),
        StatisticData(
            name = context.getString(R.string.starvation_statistic_favorite_plan_text),
            value = favouritePlanResId?.let { context.getString(favouritePlanResId) }
                ?: context.getString(R.string.starvation_statistic_not_enough_data_text)
        )
    )
}

@Composable
private fun NotStarvingContent(
    state: StarvationViewModel.StarvationState.NotStarving,
    onCreateStarvationTrack: () -> Unit
) {
    val context = LocalContext.current
    val starvationPlanResourcesProvider = LocalStarvationPlanResourcesProvider.current

    val statisticData = remember(state.starvationStatistic) {
        state.starvationStatistic.toStarvationStatistic(
            context = context,
            favouritePlanResId = state.starvationStatistic.favouritePlan?.let {
                starvationPlanResourcesProvider.provide(it).nameResId
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
            Title(text = stringResource(id = R.string.starvation_lastStarvationTracks_text))
            Spacer(modifier = Modifier.height(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                if (state.lastStarvationTracks.isEmpty()) {
                    Description(text = stringResource(id = R.string.starvation_noEnoughDataToShowLastTracks_text))
                } else {
                    state.lastStarvationTracks.forEach {
                        StarvationItem(starvationTrack = it)
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Title(text = stringResource(id = R.string.starvation_statistic_text))
            Spacer(modifier = Modifier.height(24.dp))
            Statistics(
                modifier = Modifier.fillMaxWidth(),
                statistics = statisticData
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        IconTextButton(
            iconResId = R.drawable.ic_play,
            labelResId = R.string.starvation_startStarvation_buttonText,
            onClick = onCreateStarvationTrack
        )
    }
}

@Composable
private fun StarvingContent(
    state: StarvationViewModel.StarvationState.Starving,
    onEndStarvation: () -> Unit
) {
    val context = LocalContext.current
    val starvationPlanResourcesProvider = LocalStarvationPlanResourcesProvider.current
    val dateTimeFormatter = LocalDateTimeFormatter.current
    val formattedDate = dateTimeFormatter.formatDateTime(state.startTimeInMillis)

    val starvationPlanResources = starvationPlanResourcesProvider.provide(state.selectedPlan)

    val statisticData = remember(state.starvationStatistic) {
        state.starvationStatistic.toStarvationStatistic(
            context = context,
            favouritePlanResId = state.starvationStatistic.favouritePlan?.let {
                starvationPlanResourcesProvider.provide(it).nameResId
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
                text = stringResource(id = R.string.starvation_you_starving_text),
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
                    id = R.string.starvation_startDate_text,
                    formatArgs = arrayOf(formattedDate)
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Description(
                text = stringResource(
                    id = R.string.starvation_yourSelectedPlan_formatText,
                    formatArgs = arrayOf(stringResource(id = starvationPlanResources.nameResId))
                )
            )
            Spacer(modifier = Modifier.height(32.dp))
            Title(text = stringResource(id = R.string.starvation_statistic_text))
            Spacer(modifier = Modifier.height(32.dp))
            Statistics(statistics = statisticData)
        }
        Spacer(modifier = Modifier.height(32.dp))
        IconTextButton(
            iconResId = R.drawable.ic_close,
            labelResId = R.string.starvation_endStarvation_buttonText,
            onClick = onEndStarvation
        )
    }
}

@Composable
private fun FinishStarvingContent(
    state: StarvationViewModel.StarvationState.Finished,
    onClose: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val dateTimeFormatter = LocalDateTimeFormatter.current

    val formattedStarvationTime = dateTimeFormatter.formatMillisDistance(
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
            id = R.string.starvation_finish_fail_results_text,
            formatArgs = arrayOf(formattedStarvationTime)
        )
    } else {
        stringResource(id = R.string.starvation_finish_success_results_text)
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
                painter = painterResource(id = R.drawable.starvation_finish_image),
                contentDescription = stringResource(id = R.string.starvation_finish_image_contentDescription)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Headline(text = stringResource(id = R.string.starvation_finish_text))
            Spacer(modifier = Modifier.height(16.dp))
            Description(text = advicesStringResource)
        }
        IconTextButton(
            iconResId = R.drawable.ic_navigate_next,
            labelResId = R.string.starvation_finish_goNext_buttonText,
            onClick = onClose
        )
    }
}