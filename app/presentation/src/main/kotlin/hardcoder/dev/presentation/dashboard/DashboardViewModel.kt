package hardcoder.dev.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.ToggleController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logic.features.diary.DiaryDailyRateProvider
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackProvider
import hardcoder.dev.logic.features.fasting.track.CurrentFastingManager
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackDailyRateProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackProvider
import hardcoder.dev.logic.features.pedometer.PedometerDailyRateStepsProvider
import hardcoder.dev.logic.features.pedometer.PedometerTrackProvider
import hardcoder.dev.logic.features.waterTracking.WaterTrackingMillilitersDrunkProvider
import hardcoder.dev.logic.hero.health.HeroHealthPointsResolver
import hardcoder.dev.logic.hero.HeroProvider
import hardcoder.dev.logic.reward.experience.HeroExperiencePointsProgressResolver
import hardcoder.dev.logic.reward.experience.HeroExperiencePointsProvider
import hardcoder.dev.math.safeDiv
import hardcoder.dev.presentation.features.pedometer.PedometerManager
import hardcoder.dev.presentation.features.pedometer.toggleTracking
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class DashboardViewModel(
    private val pedometerDailyRateStepsProvider: PedometerDailyRateStepsProvider,
    private val pedometerManager: PedometerManager,
    private val dateTimeProvider: DateTimeProvider,
    private val waterTrackingMillilitersDrunkProvider: WaterTrackingMillilitersDrunkProvider,
    private val pedometerTrackProvider: PedometerTrackProvider,
    private val moodTrackProvider: MoodTrackProvider,
    private val moodTrackDailyRateProvider: MoodTrackDailyRateProvider,
    private val currentFastingManager: CurrentFastingManager,
    private val diaryTrackProvider: DiaryTrackProvider,
    private val diaryDailyRateProvider: DiaryDailyRateProvider,
    private val heroHealthPointsResolver: HeroHealthPointsResolver,
    private val heroExperiencePointsResolver: HeroExperiencePointsProgressResolver,
    private val heroExperiencePointsProvider: HeroExperiencePointsProvider,
    heroProvider: HeroProvider,
) : ViewModel() {

    // TODO TO USER SECTION VIEW MODEL
    val healthPointsLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = heroProvider.provideHero().filterNotNull().map {
            it.currentHealthPoints
        },
    )

    val featuresLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = combine(
            diaryItem(),
            waterTrackingItem(),
            pedometerItem(),
            fastingItem(),
            moodTrackingItemFlow(),
        ) { waterTrackingItem, pedometerItem, fastingItem, moodTrackingItem, diaryItem ->
            listOf(
                waterTrackingItem,
                pedometerItem,
                fastingItem,
                moodTrackingItem,
                diaryItem,
            )
        },
    )

    val heroLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = heroProvider.provideHero().filterNotNull().map { hero ->
            val experiencePointsToNextLevel = heroExperiencePointsProvider.getExperiencePointsToNextLevel(
                currentLevel = hero.level
            )

            DashboardHeroItem.HeroSection(
                hero = hero,
                healthPointsProgress = heroHealthPointsResolver.resolve(requireNotNull(hero.currentHealthPoints)),
                experiencePointsToNextLevel = experiencePointsToNextLevel,
                experiencePointsProgress = heroExperiencePointsResolver.resolve(
                    currentExperiencePoints = hero.experiencePoints,
                    experiencePointsNeed = experiencePointsToNextLevel,
                ),
            )
        },
    )

    val pedometerToggleController = ToggleController(
        coroutineScope = viewModelScope,
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

    private fun fastingItem() = combine(
        dateTimeProvider.currentTimeFlow(),
        currentFastingManager.provideCurrentFastingTrack(),
    ) { currentTime, currentFastingTrack ->
        currentFastingTrack?.let {
            val timeLeftInMillis = currentTime.toInstant(
                TimeZone.currentSystemDefault(),
            ) - currentFastingTrack.startTime

            DashboardFeatureItem.FastingFeature(
                timeLeftDuration = timeLeftInMillis,
                planDuration = currentFastingTrack.duration,
                progress = timeLeftInMillis.inWholeMilliseconds safeDiv requireNotNull(
                    currentFastingTrack.duration,
                ).inWholeMilliseconds,
            )
        } ?: DashboardFeatureItem.FastingFeature(
            timeLeftDuration = null,
            planDuration = null,
            progress = 0.0f,
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