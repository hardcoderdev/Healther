package hardcoder.dev.presentation.features.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.input.validateAndRequire
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.MultiSelectionController
import hardcoder.dev.controller.selection.requireSelectedItems
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logic.features.FeatureType
import hardcoder.dev.logic.features.diary.DiaryDailyRateProvider
import hardcoder.dev.logic.features.diary.diaryAttachment.DiaryAttachmentGroup
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTagProvider
import hardcoder.dev.logic.features.diary.diaryTrack.CorrectDiaryTrackContent
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackContentValidator
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackCreator
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackProvider
import hardcoder.dev.logic.reward.currency.CurrencyCalculator
import hardcoder.dev.logic.reward.currency.CurrencyCreator
import hardcoder.dev.logic.reward.currency.CurrencyType
import hardcoder.dev.logic.reward.currency.RewardableAction
import hardcoder.dev.logic.reward.experience.ExperienceAction
import hardcoder.dev.logic.reward.experience.ExperienceCalculator
import hardcoder.dev.logic.reward.experience.ExperienceCreator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

private const val ONE_DIARY_TRACK_COUNT = 1

class DiaryCreationViewModel(
    private val diaryTrackCreator: DiaryTrackCreator,
    private val dateTimeProvider: DateTimeProvider,
    diaryDailyRateProvider: DiaryDailyRateProvider,
    diaryTagProvider: DiaryTagProvider,
    diaryTrackProvider: DiaryTrackProvider,
    diaryTrackContentValidator: DiaryTrackContentValidator,
    currencyCreator: CurrencyCreator,
    currencyCalculator: CurrencyCalculator,
    experienceCreator: ExperienceCreator,
    experienceCalculator: ExperienceCalculator,
) : ViewModel() {

    private val diaryTrackList = diaryTrackProvider.provideAllDiaryTracksByDateRange(
        dateRange = dateTimeProvider.currentDateRange(),
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList(),
    )

    val contentController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = diaryTrackContentValidator::validate,
    )

    val tagMultiSelectionController = MultiSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = diaryTagProvider.provideAllDiaryTags(),
    )

    val creationController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            val diaryTrackId = diaryTrackCreator.create(
                content = contentController.validateAndRequire<CorrectDiaryTrackContent>().data,
                date = dateTimeProvider.currentInstant(),
                diaryAttachmentGroup = DiaryAttachmentGroup(
                    tags = if (tagMultiSelectionController.state.value is MultiSelectionController.State.Empty) {
                        emptySet()
                    } else {
                        tagMultiSelectionController.requireSelectedItems()
                    },
                ),
            )

            if (diaryTrackList.value.count() < diaryDailyRateProvider.provide().first()) {
               val dailyRateProgress = ONE_DIARY_TRACK_COUNT
                   .div(diaryDailyRateProvider.provide().first()).toFloat()
                   .times(100)

                experienceCreator.create(
                    date = dateTimeProvider.currentInstant(),
                    isCollected = false,
                    featureType = FeatureType.WATER_TRACKING,
                    linkedTrackId = diaryTrackId,
                    experiencePointsAmount = experienceCalculator.calculateExperienceFor(
                        experienceAction = ExperienceAction.DailyRateProgress(
                            currentProgressInPercentage = dailyRateProgress,
                        ),
                    ),
                )

                currencyCreator.create(
                    date = dateTimeProvider.currentInstant(),
                    currencyType = CurrencyType.COINS,
                    isCollected = false,
                    featureType = FeatureType.DIARY,
                    linkedTrackId = diaryTrackId,
                    currencyAmount = currencyCalculator.calculateRewardFor(
                        rewardableAction = RewardableAction.DailyRateProgress(
                            currentProgressInPercentage = dailyRateProgress,
                        ),
                    ),
                )
            }
        },
        isAllowedFlow = contentController.state.map {
            it.validationResult == null || it.validationResult is CorrectDiaryTrackContent
        },
    )
}