package hardcoder.dev.presentation.hero

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.request.SingleRequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.controller.selection.requireSelectedItem
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logic.appPreferences.AppPreference
import hardcoder.dev.logic.appPreferences.AppPreferenceUpdater
import hardcoder.dev.logic.hero.HeroCreator
import hardcoder.dev.logic.hero.gender.GenderProvider

class HeroCreationViewModel(
    appPreferenceUpdater: AppPreferenceUpdater,
    genderProvider: GenderProvider,
    dateTimeProvider: DateTimeProvider,
    heroCreator: HeroCreator,
) : ViewModel() {

    val genderSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = genderProvider.provideAllGenders(),
    )

    val weightInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = DEFAULT_WEIGHT,
    )

    val exerciseStressTimeInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = DEFAULT_EXERCISE_STRESS_TIME,
    )

    val heroCreationController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            heroCreator.createHero(
                weight = weightInputController.state.value.input,
                exerciseStressTime = exerciseStressTimeInputController.state.value.input,
                gender = genderSelectionController.requireSelectedItem(),
            )

            appPreferenceUpdater.update(
                AppPreference(
                    firstLaunchTime = dateTimeProvider.currentInstant(),
                    lastAppReviewRequestTime = null,
                ),
            )
        },
    )

    private companion object {
        private const val DEFAULT_WEIGHT = 30
        private const val DEFAULT_EXERCISE_STRESS_TIME = 0
    }
}