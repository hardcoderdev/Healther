package hardcoder.dev.mock.dataProviders.features

import hardcoder.dev.entities.features.fasting.FastingDurationStatistics
import hardcoder.dev.entities.features.fasting.FastingPlan
import hardcoder.dev.entities.features.fasting.FastingStatistics
import hardcoder.dev.entities.features.fasting.FastingTrack
import hardcoder.dev.mock.dataProviders.date.MockDateProvider
import hardcoder.dev.presentation.features.fasting.FastingChartData
import hardcoder.dev.presentation.features.fasting.FastingChartEntry
import hardcoder.dev.presentation.features.fasting.FastingViewModel

object FastingMockDataProvider {

    fun fastingTracksList() = listOf(
        FastingTrack(
            id = 0,
            startTime = MockDateProvider.instant(),
            duration = MockDateProvider.duration(),
            fastingPlan = FastingPlan.PLAN_14_10,
            interruptedTime = null,
            fastingProgress = 0.5f,
        ),
        FastingTrack(
            id = 1,
            startTime = MockDateProvider.instant(),
            duration = MockDateProvider.duration(),
            fastingPlan = FastingPlan.PLAN_16_8,
            interruptedTime = null,
            fastingProgress = 0.7f,
        ),
        FastingTrack(
            id = 2,
            startTime = MockDateProvider.instant(),
            duration = MockDateProvider.duration(),
            fastingPlan = FastingPlan.PLAN_18_6,
            interruptedTime = null,
            fastingProgress = 0.2f,
        ),
        FastingTrack(
            id = 3,
            startTime = MockDateProvider.instant(),
            duration = MockDateProvider.duration(),
            fastingPlan = FastingPlan.PLAN_20_4,
            interruptedTime = null,
            fastingProgress = 1.0f,
        ),
    )

    fun fastingState() = FastingViewModel.FastingState.Fasting(
        selectedPlan = hardcoder.dev.entities.features.fasting.FastingPlan.PLAN_16_8,
        fastingProgress = 0.5f,
        startTimeInMillis = MockDateProvider.instant(),
        timeLeftInMillis = MockDateProvider.duration(),
        durationInMillis = MockDateProvider.duration(),
    )

    fun finishedState() = FastingViewModel.FastingState.Finished(
        fastingPlan = hardcoder.dev.entities.features.fasting.FastingPlan.PLAN_16_8,
        timeLeftInMillis = MockDateProvider.duration(),
        isInterrupted = true,
    )

    fun fastingStatistics() = FastingStatistics(
        favouritePlan = FastingPlan.PLAN_14_10,
        duration = FastingDurationStatistics(
            maximumDurationInHours = 4,
            minimumDurationInHours = 2,
            averageDurationInHours = 3,
        ),
    )

    fun fastingChartData() = FastingChartData(
        entriesList = listOf(
            FastingChartEntry(
                from = 0,
                to = 1,
            ),
            FastingChartEntry(
                from = 2,
                to = 3,
            ),
            FastingChartEntry(
                from = 4,
                to = 5,
            ),
            FastingChartEntry(
                from = 6,
                to = 7,
            ),
        ),
    )
}