package hardcoder.dev.androidApp.ui.features.starvation

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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
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
import hardcoder.dev.androidApp.ui.LocalStarvationStatisticLabelResolver
import hardcoder.dev.androidApp.ui.LocalTimeUnitMapper
import hardcoder.dev.entities.features.starvation.StarvationPlan
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
                        onClose = viewModel::clearCurrentTrack
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

@Composable
private fun NotStarvingContent(
    state: StarvationViewModel.StarvationState.NotStarving.State,
    onCreateStarvationTrack: () -> Unit
) {
    val starvationPlanResourcesProvider = LocalStarvationPlanResourcesProvider.current
    val starvationStatisticLabelResolver = LocalStarvationStatisticLabelResolver.current

    val statisticData = state.statistic.statisticEntries.toMutableList().map {
        val rowLabel = stringResource(id = starvationStatisticLabelResolver.resolve(it.first))
        val rowValue = if (it.second != null) {
            it.second
        } else {
            stringResource(R.string.starvation_statistic_not_enough_data)
        }

        StatisticData(name = rowLabel, value = checkNotNull(rowValue))
    }.toMutableList()

    val favoritePlan = state.statistic.favoritePlan?.let {
        StatisticData(
            name = stringResource(id = R.string.starvation_statistic_favorite_plan_label),
            value = stringResource(id = starvationPlanResourcesProvider.provide(it).nameResId)
        )
    } ?: run {
        StatisticData(
            name = stringResource(id = R.string.starvation_statistic_favorite_plan_label),
            value = stringResource(id = R.string.starvation_statistic_not_enough_data)
        )
    }

    statisticData.add(favoritePlan)

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
                if (state.lastThreeStarvationTracks.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.starvation_noEnoughDataToShowLastTracks_text),
                        style = MaterialTheme.typography.titleMedium
                    )
                } else {
                    state.lastThreeStarvationTracks.forEach {
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
    val starvationPlanResourcesProvider = LocalStarvationPlanResourcesProvider.current
    val dateTimeFormatter = LocalDateTimeFormatter.current
    val starvationStatisticLabelResolver = LocalStarvationStatisticLabelResolver.current

    val starvationPlanResources =
        starvationPlanResourcesProvider.provide(state.selectedPlan)

    val formattedDate = dateTimeFormatter.formatDateTime(state.startTimeInMillis)
    val statisticData = state.statistic.statisticEntries.toMutableList().map {
        val rowLabel = stringResource(id = starvationStatisticLabelResolver.resolve(it.first))
        val rowValue = it.second ?: run { stringResource(R.string.starvation_statistic_not_enough_data) }

        StatisticData(name = rowLabel, value = rowValue)
    }.toMutableList()

    val favoritePlan = state.statistic.favoritePlan?.let {
        StatisticData(
            name = stringResource(id = R.string.starvation_statistic_favorite_plan_label),
            value = stringResource(id = starvationPlanResourcesProvider.provide(it).nameResId)
        )
    } ?: run {
        StatisticData(
            name = stringResource(id = R.string.starvation_statistic_favorite_plan_label),
            value = stringResource(id = R.string.starvation_statistic_not_enough_data)
        )
    }

    statisticData.add(favoritePlan)


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
                    formatArgs = arrayOf(stringResource(id = starvationPlanResources.nameResId))
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
    val timeUnitMapper = LocalTimeUnitMapper.current

    val isCustomPlan = state.starvationPlan == StarvationPlan.CUSTOM_PLAN
    val isInterrupted = state.interruptedMillis != null

    val hoursInMillis = state.interruptedMillis?.let {
        it - state.startTimeInMillis
    } ?: run {
        timeUnitMapper.hoursToMillis(
            starvationPlanResourcesProvider.provide(state.starvationPlan).starvingHoursCount.toLong()
        )
    }

    val minutesInMillis = state.interruptedMillis?.let {
        it - state.startTimeInMillis
    } ?: run {
        0
    }

    val hoursCount = if (!isCustomPlan) {
        if (!isInterrupted) {
            starvationPlanResourcesProvider.provide(state.starvationPlan).starvingHoursCount.toLong()
        } else {
            timeUnitMapper.millisToHours(hoursInMillis)
        }
    } else {
        timeUnitMapper.millisToHours(hoursInMillis)
    }

    val minutesCount = if (!isCustomPlan) {
        if (!isInterrupted) {
            0
        } else {
            timeUnitMapper.millisToMinutes(minutesInMillis) - (hoursCount * 60)
        }
    } else {
        timeUnitMapper.millisToMinutes(minutesInMillis) - (hoursCount * 60)
    }

    val hoursPlurals = pluralStringResource(id = R.plurals.hours, count = hoursCount.toInt())
    val minutesPlurals = pluralStringResource(id = R.plurals.minutes, count = minutesCount.toInt())

    val advicesStringResource = if (isCustomPlan) {
        stringResource(
            id = R.string.starvation_finish_custom_results_text,
            formatArgs = arrayOf(hoursCount, hoursPlurals, minutesCount, minutesPlurals)
        )
    } else if (isInterrupted) {
        stringResource(
            R.string.starvation_finish_fail_results_text,
            formatArgs = arrayOf(hoursCount, hoursPlurals, minutesCount, minutesPlurals)
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