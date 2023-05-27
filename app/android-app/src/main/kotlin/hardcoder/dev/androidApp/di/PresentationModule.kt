package hardcoder.dev.androidApp.di

import android.content.Context
import hardcoder.dev.logic.hero.gender.Gender
import hardcoder.dev.presentation.dashboard.DashboardViewModel
import hardcoder.dev.presentation.features.diary.DiaryCreateTrackViewModel
import hardcoder.dev.presentation.features.diary.DiaryUpdateTrackViewModel
import hardcoder.dev.presentation.features.diary.DiaryViewModel
import hardcoder.dev.presentation.features.diary.tags.CreateTagViewModel
import hardcoder.dev.presentation.features.diary.tags.ManageTagsViewModel
import hardcoder.dev.presentation.features.diary.tags.UpdateTagViewModel
import hardcoder.dev.presentation.features.fasting.FastingHistoryViewModel
import hardcoder.dev.presentation.features.fasting.FastingTrackCreateViewModel
import hardcoder.dev.presentation.features.fasting.FastingViewModel
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingHistoryViewModel
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingTrackCreateViewModel
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingTrackUpdateViewModel
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingViewModel
import hardcoder.dev.presentation.features.moodTracking.activity.CreateActivityViewModel
import hardcoder.dev.presentation.features.moodTracking.activity.ManageActivitiesViewModel
import hardcoder.dev.presentation.features.moodTracking.activity.UpdateActivityViewModel
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypeCreateViewModel
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypeManageTracksViewModel
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypeUpdateViewModel
import hardcoder.dev.presentation.features.pedometer.PedometerHistoryViewModel
import hardcoder.dev.presentation.features.pedometer.PedometerViewModel
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingCreateViewModel
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingHistoryViewModel
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingUpdateViewModel
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingViewModel
import hardcoder.dev.presentation.features.waterTracking.drinkType.DrinkTypeCreateViewModel
import hardcoder.dev.presentation.features.waterTracking.drinkType.DrinkTypeManageViewModel
import hardcoder.dev.presentation.features.waterTracking.drinkType.DrinkTypeUpdateViewModel
import hardcoder.dev.presentation.setUpFlow.EnterExerciseStressTimeViewModel
import hardcoder.dev.presentation.setUpFlow.EnterWeightViewModel
import hardcoder.dev.presentation.setUpFlow.HeroCreateViewModel
import hardcoder.dev.presentation.setUpFlow.SelectGenderViewModel
import hardcoder.dev.presentation.setUpFlow.SplashViewModel
import hardcoder.dev.presentation.settings.SettingsViewModel

class PresentationModule(
    val context: Context,
    val logicModule: LogicModule
) {
    fun getSplashViewModel() = SplashViewModel(
        appPreferenceProvider = logicModule.appPreferenceProvider
    )

    fun getEnterWeightViewModel() = EnterWeightViewModel()

    fun getSelectGenderViewModel() = SelectGenderViewModel(
        genderProvider = logicModule.genderProvider
    )

    fun getEnterExerciseStressTimeViewModel() = EnterExerciseStressTimeViewModel()

    fun getDashboardViewModel() = DashboardViewModel(
        dailyRateStepsResolver = logicModule.dailyRateStepsResolver,
        pedometerTrackProvider = logicModule.pedometerTrackProvider,
        moodTrackProvider = logicModule.moodTrackProvider,
        pedometerManager = logicModule.pedometerManager,
        currentFastingManager = logicModule.currentFastingManager,
        dateTimeProvider = logicModule.dateTimeProvider,
        waterTrackingDailyRateProvider = logicModule.waterTrackingDailyRateProvider,
        waterTrackingMillilitersDrunkProvider = logicModule.waterTrackingMillilitersDrunkProvider
    )

    fun getSettingsViewModel() = SettingsViewModel(
        reviewManager = logicModule.reviewManager,
        appPreferenceProvider = logicModule.appPreferenceProvider,
        appPreferenceUpdater = logicModule.appPreferenceUpdater
    )

    fun getHeroCreateViewModel(
        gender: Gender,
        weight: Int,
        exerciseStressTime: Int
    ) = HeroCreateViewModel(
        heroCreator = logicModule.heroCreator,
        appPreferenceUpdater = logicModule.appPreferenceUpdater,
        gender = gender,
        weight = weight,
        exerciseStressTime = exerciseStressTime
    )

    fun getWaterTrackingViewModel() = WaterTrackingViewModel(
        waterTrackProvider = logicModule.waterTrackProvider,
        waterPercentageResolver = logicModule.waterPercentageResolver,
        waterTrackingStatisticProvider = logicModule.waterTrackingStatisticProvider,
        millilitersDrunkProvider = logicModule.waterTrackingMillilitersDrunkProvider
    )

    fun getDrinkTypeManageTracksViewModel() = DrinkTypeManageViewModel(
        drinkTypeProvider = logicModule.drinkTypeProvider
    )

    fun getDrinkTypeCreateViewModel() = DrinkTypeCreateViewModel(
        iconResourceProvider = logicModule.drinkTypeIconProvider,
        drinkTypeCreator = logicModule.drinkTypeCreator,
        drinkTypeNameValidator = logicModule.drinkTypeNameValidator
    )

    fun getDrinkTypeUpdateViewModel(drinkTypeId: Int) = DrinkTypeUpdateViewModel(
        drinkTypeId = drinkTypeId,
        drinkTypeUpdater = logicModule.drinkTypeUpdater,
        drinkTypeDeleter = logicModule.drinkTypeDeleter,
        drinkTypeProvider = logicModule.drinkTypeProvider,
        drinkTypeNameValidator = logicModule.drinkTypeNameValidator,
        iconResourceProvider = logicModule.drinkTypeIconProvider
    )

    fun getWaterTrackCreateViewModel() = WaterTrackingCreateViewModel(
        waterTrackCreator = logicModule.waterTrackCreator,
        drinkTypeProvider = logicModule.drinkTypeProvider,
        waterTrackMillilitersValidator = logicModule.waterTrackMillilitersValidator,
        dateTimeProvider = logicModule.dateTimeProvider,
        waterTrackingDailyRateProvider = logicModule.waterTrackingDailyRateProvider
    )

    fun getWaterTrackUpdateViewModel(waterTrackId: Int) = WaterTrackingUpdateViewModel(
        waterTrackId = waterTrackId,
        waterTrackUpdater = logicModule.waterTrackUpdater,
        waterTrackProvider = logicModule.waterTrackProvider,
        drinkTypeProvider = logicModule.drinkTypeProvider,
        waterTrackMillilitersValidator = logicModule.waterTrackMillilitersValidator,
        waterTrackDeleter = logicModule.waterTrackDeleter,
        waterTrackingDailyRateProvider = logicModule.waterTrackingDailyRateProvider
    )

    fun getWaterTrackingHistoryViewModel() = WaterTrackingHistoryViewModel(
        waterTrackProvider = logicModule.waterTrackProvider,
        waterPercentageResolver = logicModule.waterPercentageResolver
    )

    fun getPedometerViewModel() = PedometerViewModel(
        pedometerManager = logicModule.pedometerManager,
        pedometerTrackProvider = logicModule.pedometerTrackProvider,
        pedometerStatisticProvider = logicModule.pedometerStatisticProvider
    )

    fun getPedometerHistoryViewModel() = PedometerHistoryViewModel(
        pedometerTrackProvider = logicModule.pedometerTrackProvider,
        pedometerStatisticProvider = logicModule.pedometerStatisticProvider
    )

    fun getFastingViewModel() = FastingViewModel(
        dateTimeProvider = logicModule.dateTimeProvider,
        fastingTrackProvider = logicModule.fastingTrackProvider,
        statisticProvider = logicModule.fastingStatisticProvider,
        currentFastingManager = logicModule.currentFastingManager
    )

    fun getFastingTrackCreateViewModel() = FastingTrackCreateViewModel(
        currentFastingManager = logicModule.currentFastingManager,
        fastingPlanDurationMapper = logicModule.fastingPlanDurationResolver,
        fastingPlanProvider = logicModule.fastingPlanProvider
    )

    fun getFastingHistoryViewModel() = FastingHistoryViewModel(
        fastingTrackProvider = logicModule.fastingTrackProvider
    )

    fun getMoodTrackingViewModel() = MoodTrackingViewModel(
        moodWithActivitiesProvider = logicModule.moodWithActivitiesProvider,
        moodTrackProvider = logicModule.moodTrackProvider,
        dateTimeProvider = logicModule.dateTimeProvider,
        moodTrackingStatisticProvider = logicModule.moodTrackingStatisticProvider
    )

    fun getMoodTrackingCreateViewModel() = MoodTrackingTrackCreateViewModel(
        moodTrackCreator = logicModule.moodTrackCreator,
        moodTypeProvider = logicModule.moodTypeProvider,
        activityProvider = logicModule.activityProvider
    )

    fun getMoodTrackingUpdateViewModel(moodTrackId: Int) = MoodTrackingTrackUpdateViewModel(
        moodTrackId = moodTrackId,
        moodTrackUpdater = logicModule.moodTrackUpdater,
        moodTrackDeleter = logicModule.moodTrackDeleter,
        diaryTrackProvider = logicModule.diaryTrackProvider,
        moodTrackProvider = logicModule.moodTrackProvider,
        diaryAttachmentProvider = logicModule.diaryAttachmentProvider,
        moodWithActivityProvider = logicModule.moodWithActivitiesProvider,
        activityProvider = logicModule.activityProvider,
        moodTypeProvider = logicModule.moodTypeProvider
    )

    fun getMoodTrackingHistoryViewModel() = MoodTrackingHistoryViewModel(
        moodTrackDeleter = logicModule.moodTrackDeleter,
        moodWithActivitiesProvider = logicModule.moodWithActivitiesProvider
    )

    fun getMoodTypeManageTracksViewModel() = MoodTypeManageTracksViewModel(
        moodTypeProvider = logicModule.moodTypeProvider
    )

    fun getMoodTypeTrackCreateViewModel() = MoodTypeCreateViewModel(
        moodTypeCreator = logicModule.moodTypeCreator,
        moodTypeNameValidator = logicModule.moodTypeNameValidator,
        iconResourceProvider = logicModule.moodTypeIconProvider
    )

    fun getMoodTypeTrackUpdateViewModel(moodTypeId: Int) = MoodTypeUpdateViewModel(
        moodTypeId = moodTypeId,
        iconResourceProvider = logicModule.moodTypeIconProvider,
        moodTypeNameValidator = logicModule.moodTypeNameValidator,
        moodTypeProvider = logicModule.moodTypeProvider,
        moodTypeUpdater = logicModule.moodTypeUpdater,
        moodTypeDeleter = logicModule.moodTypeDeleter
    )

    fun getManageActivitiesViewModel() = ManageActivitiesViewModel(
        activityProvider = logicModule.activityProvider
    )

    fun getCreateActivityViewModel() = CreateActivityViewModel(
        activityCreator = logicModule.activityCreator,
        activityNameValidator = logicModule.activityNameValidator,
        iconResourceProvider = logicModule.activityIconProvider
    )

    fun getUpdateActivityViewModel(hobbyTrackId: Int) = UpdateActivityViewModel(
        activityId = hobbyTrackId,
        activityNameValidator = logicModule.activityNameValidator,
        activityDeleter = logicModule.activityDeleter,
        activityUpdater = logicModule.activityUpdater,
        activityProvider = logicModule.activityProvider,
        iconResourceProvider = logicModule.activityIconProvider
    )

    fun getDiaryViewModel() = DiaryViewModel(
        dateRangeFilterTypeMapper = logicModule.dateRangeFilterTypeMapper,
        dateRangeFilterTypeProvider = logicModule.dateRangeFilterTypeProvider,
        diaryTrackProvider = logicModule.diaryTrackProvider,
        diaryTagProvider = logicModule.diaryTagProvider
    )

    fun getDiaryCreateTrackViewModel() = DiaryCreateTrackViewModel(
        diaryTrackCreator = logicModule.diaryTrackCreator,
        diaryTrackContentValidator = logicModule.diaryTrackContentValidator,
        diaryTagProvider = logicModule.diaryTagProvider
    )

    fun getDiaryUpdateTrackViewModel(diaryTrackId: Int) = DiaryUpdateTrackViewModel(
        diaryTrackId = diaryTrackId,
        diaryTrackUpdater = logicModule.diaryTrackUpdater,
        diaryTrackProvider = logicModule.diaryTrackProvider,
        diaryTrackDeleter = logicModule.diaryTrackDeleter,
        diaryTagProvider = logicModule.diaryTagProvider,
        diaryTrackContentValidator = logicModule.diaryTrackContentValidator
    )

    fun getManageTagsViewModel() = ManageTagsViewModel(
        diaryTagProvider = logicModule.diaryTagProvider
    )

    fun getCreateTagViewModel() = CreateTagViewModel(
        diaryTagCreator = logicModule.diaryTagCreator,
        diaryTagNameValidator = logicModule.diaryTagNameValidator,
        iconResourceProvider = logicModule.diaryTagIconProvider
    )

    fun getUpdateTagViewModel(diaryTagId: Int) = UpdateTagViewModel(
        tagId = diaryTagId,
        diaryTagNameValidator = logicModule.diaryTagNameValidator,
        iconResourceProvider = logicModule.diaryTagIconProvider,
        diaryTagProvider = logicModule.diaryTagProvider,
        diaryTagUpdater = logicModule.diaryTagUpdater,
        diaryTagDeleter = logicModule.diaryTagDeleter
    )
}