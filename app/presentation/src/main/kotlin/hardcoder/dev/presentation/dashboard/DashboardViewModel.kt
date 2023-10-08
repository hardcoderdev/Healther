package hardcoder.dev.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logic.features.diary.DiaryDailyRateProvider
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackDailyRateProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackProvider
import hardcoder.dev.logic.features.pedometer.PedometerDailyRateStepsProvider
import hardcoder.dev.logic.features.pedometer.PedometerTrackProvider
import hardcoder.dev.logic.features.waterTracking.WaterTrackingMillilitersDrunkProvider
import hardcoder.dev.math.safeDiv
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class DashboardViewModel(
    private val pedometerDailyRateStepsProvider: PedometerDailyRateStepsProvider,
    //private val pedometerManager: PedometerManager, TODO UNCOMMENT
    private val dateTimeProvider: DateTimeProvider,
    private val waterTrackingMillilitersDrunkProvider: WaterTrackingMillilitersDrunkProvider,
    private val pedometerTrackProvider: PedometerTrackProvider,
    private val moodTrackProvider: MoodTrackProvider,
    private val moodTrackDailyRateProvider: MoodTrackDailyRateProvider,
    private val diaryTrackProvider: DiaryTrackProvider,
    private val diaryDailyRateProvider: DiaryDailyRateProvider,
) : ViewModel() {

    val featuresLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = combine(
            diaryItem(),
            waterTrackingItem(),
            //pedometerItem(), TODO UNCOMMENT
            moodTrackingItemFlow(),
        ) {
                waterTrackingItem, // pedometerItem, TODO UNCOMMENT AND ONE-LINE
                moodTrackingItem, diaryItem,
            ->
            listOf(
                waterTrackingItem,
                //pedometerItem,
                moodTrackingItem,
                diaryItem,
            )
        },
    )

//    TODO UNCOMMENT
//    val pedometerToggleController = ToggleController(
//        coroutineScope = viewModelScope,
//        toggle = pedometerManager::toggleTracking,
//        isActiveFlow = pedometerManager.isTracking,
//    )

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
//        pedometerManager.isTracking,
//        pedometerManager.availability, TODO UNCOMMENT
    ) { pedometerTrackList, dailyRateInSteps -> //isPedometerRunning, availability -> TODO UNCOMMENT
        val stepsWalked = pedometerTrackList.sumOf { it.stepsCount }
        DashboardFeatureItem.PedometerFeature(
            stepsWalked = stepsWalked,
            dailyRateInSteps = dailyRateInSteps,
            isPedometerRunning = false, // isPedometerRunning, TODO UNCOMMENT
            isPermissionsGranted = true, //availability is PedometerManager.Availability.Available, TODO UNCOMMENT
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