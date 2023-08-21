package hardcoder.dev.presentation.features.moodTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.input.getInput
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.MultiSelectionController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.controller.selection.requireSelectedItem
import hardcoder.dev.controller.selection.selectedItemsOrEmptySet
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.toInstant
import hardcoder.dev.logic.reward.currency.CurrencyType
import hardcoder.dev.logic.reward.currency.CurrencyCalculator
import hardcoder.dev.logic.reward.currency.CurrencyCreator
import hardcoder.dev.logic.reward.currency.RewardableAction
import hardcoder.dev.logic.features.FeatureType
import hardcoder.dev.logic.features.moodTracking.moodActivity.MoodActivityProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackCreator
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackDailyRateProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackProvider
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeProvider
import hardcoder.dev.logic.reward.experience.ExperienceAction
import hardcoder.dev.logic.reward.experience.ExperienceCalculator
import hardcoder.dev.logic.reward.experience.ExperienceCreator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn

private const val ONE_MOOD_TRACK = 1F

class MoodTrackingCreationViewModel(
    private val moodTrackCreator: MoodTrackCreator,
    dateTimeProvider: DateTimeProvider,
    moodTypeProvider: MoodTypeProvider,
    moodTrackProvider: MoodTrackProvider,
    moodActivityProvider: MoodActivityProvider,
    moodTrackDailyRateProvider: MoodTrackDailyRateProvider,
    currencyCreator: CurrencyCreator,
    currencyCalculator: CurrencyCalculator,
    experienceCreator: ExperienceCreator,
    experienceCalculator: ExperienceCalculator,
) : ViewModel() {

    private val moodTrackList = moodTrackProvider.provideAllMoodTracksByDayRange(
        dayRange = dateTimeProvider.currentDateRange(),
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList(),
    )

    val dateController = InputController(
        coroutineScope = viewModelScope,
        initialInput = dateTimeProvider.currentDate(),
    )

    val timeInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = dateTimeProvider.currentTime().time,
    )

    val moodTypeSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = moodTypeProvider.provideAllMoodTypes(),
    )

    val noteController = InputController(
        coroutineScope = viewModelScope,
        initialInput = "",
    )

    val activitiesMultiSelectionController = MultiSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = moodActivityProvider.provideAllActivities(),
    )

    val creationController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            val moodTrackId = moodTrackCreator.create(
                note = noteController.state.value.input.ifEmpty { null },
                moodType = moodTypeSelectionController.requireSelectedItem(),
                date = dateController.getInput().toInstant(timeInputController.getInput()),
                selectedActivities = activitiesMultiSelectionController.selectedItemsOrEmptySet().first(),
            )

            if (moodTrackList.value.count() < moodTrackDailyRateProvider.provide().first()) {
                val dailyRateProgress = ONE_MOOD_TRACK
                    .div(moodTrackDailyRateProvider.provide().first().toFloat())
                    .times(100)

                experienceCreator.create(
                    date = dateTimeProvider.currentInstant(),
                    isCollected = false,
                    featureType = FeatureType.MOOD_TRACKING,
                    linkedTrackId = moodTrackId,
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
                    featureType = FeatureType.MOOD_TRACKING,
                    linkedTrackId = moodTrackId,
                    currencyAmount = currencyCalculator.calculateRewardFor(
                        rewardableAction = RewardableAction.DailyRateProgress(
                            currentProgressInPercentage = dailyRateProgress,
                        ),
                    ),
                )
            }
        },
    )
}