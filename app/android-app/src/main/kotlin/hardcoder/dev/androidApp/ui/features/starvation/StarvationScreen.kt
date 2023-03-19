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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Start
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.ui.DateTimeFormatter
import hardcoder.dev.androidApp.ui.LocalDateTimeFormatter
import hardcoder.dev.androidApp.ui.LocalPresentationModule
import hardcoder.dev.androidApp.ui.LocalStarvationPlanResourcesProvider
import hardcoder.dev.entities.features.starvation.StarvationPlan
import hardcoder.dev.entities.features.starvation.statistic.StarvationStatistic
import hardcoder.dev.extensions.safeDiv
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.features.starvation.StarvationViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.CircularProgressBar
import hardcoder.dev.uikit.IconTextButton
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.StatisticData
import hardcoder.dev.uikit.Statistics
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType

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
                is StarvationViewModel.StarvationState.NotStarving.State -> {
                    NotStarvingContent(
                        state = starvationState,
                        onCreateStarvationTrack = onCreateStarvationTrack
                    )
                }

                is StarvationViewModel.StarvationState.Starving.State -> {
                    StarvingContent(
                        state = starvationState,
                        onEndStarvation = viewModel::interruptTrack
                    )
                }

                is StarvationViewModel.StarvationState.Finished.State -> {
                    FinishStarvingContent(
                        state = starvationState,
                        onClose = viewModel::clearStarvation
                    )
                }
            }
        },
        actionConfig = if (state.value is StarvationViewModel.StarvationState.NotStarving.State) {
            ActionConfig(
                actions = listOf(
                    Action(
                        iconImageVector = Icons.Filled.MoreVert,
                        onActionClick = onHistoryDetails
                    )
                )
            )
        } else {
            null
        },
        topBarConfig = if (state.value is StarvationViewModel.StarvationState.Finished.State) {
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
            name = context.getString(R.string.starvation_statistic_max_hours_label),
            value = starvationDurationStatistic?.maximumDurationInHours?.toString()
                ?: context.getString(R.string.starvation_statistic_not_enough_data)
        ),
        StatisticData(
            name = context.getString(R.string.starvation_statistic_min_hours_label),
            value = starvationDurationStatistic?.minimumDurationInHours?.toString()
                ?: context.getString(R.string.starvation_statistic_not_enough_data)
        ),
        StatisticData(
            name = context.getString(R.string.starvation_statistic_average_hours_label),
            value = starvationDurationStatistic?.averageDurationInHours?.toString()
                ?: context.getString(R.string.starvation_statistic_not_enough_data)
        ),
        StatisticData(
            name = context.getString(R.string.starvation_statistic_completion_percentage_label),
            value = percentageCompleted?.toString()
                ?: context.getString(R.string.starvation_statistic_not_enough_data)
        ),
        StatisticData(
            name = context.getString(R.string.starvation_statistic_favorite_plan_label),
            value = favouritePlanResId?.let { context.getString(favouritePlanResId) }
                ?: context.getString(R.string.starvation_statistic_not_enough_data)
        )
    )
}

@Composable
private fun NotStarvingContent(
    state: StarvationViewModel.StarvationState.NotStarving.State,
    onCreateStarvationTrack: () -> Unit
) {
    val context = LocalContext.current
    val starvationPlanResourcesProvider = LocalStarvationPlanResourcesProvider.current

    val statisticData by remember {
        mutableStateOf(
            state.starvationStatistic.toStarvationStatistic(
                context = context,
                favouritePlanResId = state.starvationStatistic.favouritePlan?.let {
                    starvationPlanResourcesProvider.provide(it).nameResId
                }
            )
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
            Text(
                text = stringResource(id = R.string.starvation_lastStarvationTracks_text),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                if (state.lastStarvationTracks.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.starvation_noEnoughDataToShowLastTracks_text),
                        style = MaterialTheme.typography.titleMedium
                    )
                } else {
                    state.lastStarvationTracks.forEach {
                        StarvationItem(starvationTrack = it)
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = stringResource(id = R.string.starvation_statistic_text),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(24.dp))
            Statistics(
                modifier = Modifier.fillMaxWidth(),
                statistics = statisticData
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        IconTextButton(
            imageVector = Icons.Filled.Start,
            labelResId = R.string.starvationScreen_startStarvation_buttonText,
            onClick = onCreateStarvationTrack
        )
    }
}

@Composable
private fun StarvingContent(
    state: StarvationViewModel.StarvationState.Starving.State,
    onEndStarvation: () -> Unit
) {
    val context = LocalContext.current
    val starvationPlanResourcesProvider = LocalStarvationPlanResourcesProvider.current
    val dateTimeFormatter = LocalDateTimeFormatter.current
    val formattedDate = dateTimeFormatter.formatDateTime(state.startTimeInMillis)

    val statisticData by remember {
        mutableStateOf(
            state.starvationStatistic.toStarvationStatistic(
                context = context,
                favouritePlanResId = state.starvationStatistic.favouritePlan?.let {
                    starvationPlanResourcesProvider.provide(it).nameResId
                }
            )
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
            Text(
                text = stringResource(id = R.string.starvationScreen_you_starving_text),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(32.dp))
            CircularProgressBar(
                modifier = Modifier.align(CenterHorizontally),
                radius = 100.dp,
                fontSize = 24.sp,
                shadowColor = MaterialTheme.colorScheme.primaryContainer,
                strokeWidth = 12.dp,
                percentage = state.timeLeftInMillis safeDiv state.durationInMillis,
                innerText = dateTimeFormatter.formatMillisDistance(
                    distanceInMillis = state.timeLeftInMillis,
                    accuracy = DateTimeFormatter.Accuracy.SECONDS
                ),
                color = if (state.selectedPlan == StarvationPlan.CUSTOM_PLAN) {
                    Color.Companion.Transparent
                } else {
                    MaterialTheme.colorScheme.primary
                }
            )
            Spacer(modifier = Modifier.height(64.dp))
            Text(
                style = MaterialTheme.typography.titleMedium,
                text = stringResource(
                    id = R.string.starvation_startDate_text,
                    formatArgs = arrayOf(formattedDate)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                style = MaterialTheme.typography.titleMedium,
                text = stringResource(
                    id = R.string.starvation_yourSelectedPlan_formatText,
                    formatArgs = arrayOf(
                        stringResource(
                            id = starvationPlanResourcesProvider.provide(
                                state.selectedPlan
                            ).nameResId
                        )
                    )
                )
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = stringResource(id = R.string.starvation_statistic_text),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(32.dp))
            Statistics(statistics = statisticData)
        }

        Spacer(modifier = Modifier.height(32.dp))
        IconTextButton(
            imageVector = Icons.Filled.Close,
            labelResId = R.string.starvationScreen_endStarvation_buttonText,
            onClick = onEndStarvation
        )
    }
}

@Composable
fun FinishStarvingContent(
    state: StarvationViewModel.StarvationState.Finished.State,
    onClose: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val starvationPlanResourcesProvider = LocalStarvationPlanResourcesProvider.current
    val dateTimeFormatter = LocalDateTimeFormatter.current

    val isCustomPlan = state.starvationPlan == StarvationPlan.CUSTOM_PLAN
    val isInterrupted = state.interruptedMillis != null

    val formattedStarvationTime = state.interruptedMillis?.let {
        dateTimeFormatter.formatMillisDistance(
            distanceInMillis = it - state.startTimeInMillis,
            accuracy = DateTimeFormatter.Accuracy.MINUTES,
            usePlurals = true
        )
    } ?: run {
        dateTimeFormatter.formatMillisDistance(
            distanceInMillis = starvationPlanResourcesProvider.provide(
                state.starvationPlan
            ).starvingHoursCount.toLong(),
            accuracy = DateTimeFormatter.Accuracy.HOURS,
            usePlurals = true
        )
    }

    val advicesStringResource = if (isCustomPlan) {
        stringResource(
            id = R.string.starvation_finish_custom_results_text,
            formatArgs = arrayOf(formattedStarvationTime)
        )
    } else if (isInterrupted) {
        stringResource(
            R.string.starvation_finish_fail_results_text,
            formatArgs = arrayOf(formattedStarvationTime)
        )
    } else {
        stringResource(
            R.string.starvation_finish_success_results_text
        )
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
                painter = painterResource(id = R.drawable.starvation_finish_image),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentDescription = stringResource(
                    id = R.string.starvation_finish_image_contentDescription
                )
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = stringResource(id = R.string.starvation_finished_text),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = advicesStringResource,
                style = MaterialTheme.typography.titleMedium
            )
        }
        IconTextButton(
            imageVector = Icons.Filled.NavigateNext,
            labelResId = R.string.starvation_finish_goNext_buttonText,
            onClick = onClose
        )
    }
}