package hardcoder.dev.presentation.dashboard

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.ToggleController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logics.features.diary.DiaryDailyRateProvider
import hardcoder.dev.logics.features.diary.diaryTrack.DiaryTrackProvider
import hardcoder.dev.logics.features.moodTracking.moodTrack.MoodTrackDailyRateProvider
import hardcoder.dev.logics.features.moodTracking.moodTrack.MoodTrackProvider
import hardcoder.dev.logics.features.pedometer.PedometerDailyRateStepsProvider
import hardcoder.dev.logics.features.pedometer.PedometerTrackProvider
import hardcoder.dev.logics.features.waterTracking.WaterTrackingMillilitersDrunkProvider
import hardcoder.dev.math.safeDiv
import hardcoder.dev.presentation.features.pedometer.PedometerManager
import hardcoder.dev.presentation.features.pedometer.toggleTracking
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class DashboardViewModel(
    private val pedometerDailyRateStepsProvider: PedometerDailyRateStepsProvider,
    private val pedometerManager: PedometerManager,
    private val dateTimeProvider: DateTimeProvider,
    private val waterTrackingMillilitersDrunkProvider: WaterTrackingMillilitersDrunkProvider,
    private val pedometerTrackProvider: PedometerTrackProvider,
    private val moodTrackProvider: MoodTrackProvider,
    private val moodTrackDailyRateProvider: MoodTrackDailyRateProvider,
    private val diaryTrackProvider: DiaryTrackProvider,
    private val diaryDailyRateProvider: DiaryDailyRateProvider,
) : ScreenModel {

    val featuresLoadingController = LoadingController(
        coroutineScope = coroutineScope,
        flow = combine(
            waterTrackingItem(),
            foodTrackingItem(),
            moodTrackingItemFlow(),
            pedometerItem(),
            diaryItem(),
        ) { waterTrackingItem, foodTrackingItem, moodTrackingItem, pedometerItem, diaryItem ->
            listOf(
                waterTrackingItem,
                foodTrackingItem,
                moodTrackingItem,
                pedometerItem,
                diaryItem,
            )
        },
    )

    val pedometerToggleController = ToggleController(
        coroutineScope = coroutineScope,
        toggle = pedometerManager::toggleTracking,
        isActiveFlow = pedometerManager.isTracking,
    )

    private fun waterTrackingItem() = waterTrackingMillilitersDrunkProvider
        .provideMillilitersDrunkToDailyRateToday(
            dateRange = dateTimeProvider.currentDateRange(),
        ).map { millilitersDrunkToDailyRate ->
            DashboardFeatureItem.WaterTrackingFeature(
                millilitersDrunk = millilitersDrunkToDailyRate,
                progress = millilitersDrunkToDailyRate.millilitersDrunkCount safeDiv millilitersDrunkToDailyRate.dailyWaterIntake,
            )
        }

    private fun pedometerItem() = combine(
        pedometerTrackProvider.providePedometerTracksByRange(dateTimeProvider.currentDateRange()),
        pedometerDailyRateStepsProvider.resolve(),
        pedometerManager.isTracking,
        pedometerManager.availability,
    ) { pedometerTrackList, dailyRateInSteps, isPedometerRunning, availability ->
        val stepsWalked = pedometerTrackList.sumOf { it.stepsCount }
        DashboardFeatureItem.PedometerFeature(
            stepsWalked = stepsWalked,
            dailyRateInSteps = dailyRateInSteps,
            isPedometerRunning = isPedometerRunning,
            isPermissionsGranted = availability is PedometerManager.Availability.Available,
            progress = stepsWalked safeDiv dailyRateInSteps,
        )
    }

    private fun moodTrackingItemFlow() = combine(
        moodTrackDailyRateProvider.provide(),
        moodTrackProvider.provideAllMoodTracksByDayRange(
            dateTimeProvider.currentDateRange(),
        ),
    ) { dailyRate, moodTrackList ->
        val moodTracksCount = moodTrackList.count()

        DashboardFeatureItem.MoodTrackingFeature(
            tracksCount = moodTracksCount,
            tracksDailyRate = dailyRate,
            progress = moodTracksCount safeDiv dailyRate,
        )
    }

    private fun foodTrackingItem() = flowOf(DashboardFeatureItem.FoodTrackingFeature)

    private fun diaryItem() = combine(
        diaryTrackProvider.provideAllDiaryTracksByDateRange(dateRange = dateTimeProvider.currentDateRange()),
        diaryDailyRateProvider.provide(),
    ) { diaryTrackList, diaryDailyRate ->
        val diaryTracksCount = diaryTrackList.count()

        DashboardFeatureItem.DiaryFeature(
            tracksCount = diaryTracksCount,
            tracksDailyRate = diaryDailyRate,
            progress = diaryTracksCount safeDiv diaryDailyRate,
        )
    }
}