package hardcoder.dev.androidApp.ui.screens.features.fasting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hardcoder.dev.androidApp.ui.formatters.DateTimeFormatter
import hardcoder.dev.androidApp.ui.formatters.MillisDistanceFormatter
import hardcoder.dev.androidApp.ui.screens.features.fasting.plans.FastingPlanResourcesProvider
import hardcoder.dev.androidApp.ui.screens.features.fasting.statistics.FastingStatisticSection
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.logic.features.fasting.statistic.FastingStatistic
import hardcoder.dev.presentation.features.fasting.FastingViewModel
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonConfig
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonWithIcon
import hardcoder.dev.uikit.components.progressBar.CircularProgressBar
import hardcoder.dev.uikit.components.section.EmptyBlock
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.text.Headline
import hardcoderdev.healther.app.android.app.R
import org.koin.compose.koinInject

@Composable
fun Fasting(
    state: FastingViewModel.FastingState.Fasting,
    fastingStatistic: FastingStatistic?,
    interruptFastingController: RequestController,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(
            Modifier
                .weight(2f)
                .verticalScroll(rememberScrollState()),
        ) {
            FastingProgressSection(state = state)
            Spacer(modifier = Modifier.height(64.dp))
            FastingInfoSection(state = state)
            Spacer(modifier = Modifier.height(16.dp))
            fastingStatistic?.let { statistic ->
                FastingStatisticSection(statistic = statistic)
            } ?: run {
                EmptyBlock(
                    emptyTitleResId = R.string.fasting_nowEmpty_text,
                    lottieAnimationResId = R.raw.empty_astronaut,
                )
            }
        }
    Spacer(modifier = Modifier.height(32.dp))
    RequestButtonWithIcon(
        requestButtonConfig = RequestButtonConfig.Filled(
            controller = interruptFastingController,
            iconResId = R.drawable.ic_close,
            labelResId = R.string.fasting_interrupt_buttonText,
        ),
    )
}
}

@Composable
private fun ColumnScope.FastingProgressSection(state: FastingViewModel.FastingState.Fasting) {
    val millisDistanceFormatter = koinInject<MillisDistanceFormatter>()

    Headline(
        text = stringResource(id = R.string.fasting_in_progress_text),
        modifier = Modifier.align(Alignment.CenterHorizontally),
    )
    Spacer(modifier = Modifier.height(32.dp))
    CircularProgressBar(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        radius = 100.dp,
        fontSize = 24.sp,
        strokeWidth = 12.dp,
        percentage = state.fastingProgress,
        innerText = millisDistanceFormatter.formatMillisDistance(
            distanceInMillis = state.timeLeftInMillis.inWholeMilliseconds,
            accuracy = MillisDistanceFormatter.Accuracy.SECONDS,
        ),
    )
}

@Composable
private fun FastingInfoSection(state: FastingViewModel.FastingState.Fasting) {
    val fastingPlanResourcesProvider = koinInject<FastingPlanResourcesProvider>()
    val dateTimeFormatter = koinInject<DateTimeFormatter>()
    val formattedDate = dateTimeFormatter.formatTime(state.startTimeInMillis)
    val fastingPlanResources = fastingPlanResourcesProvider.provide(state.selectedPlan)

    Description(
        text = stringResource(
            id = R.string.fasting_startDate_text,
            formatArgs = arrayOf(formattedDate),
        ),
    )
    Spacer(modifier = Modifier.height(12.dp))
    Description(
        text = stringResource(
            id = R.string.fasting_yourSelectedPlan_formatText,
            formatArgs = arrayOf(stringResource(id = fastingPlanResources.nameResId)),
        ),
    )
}