package hardcoder.dev.presentation.hero

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.input.getInput
import hardcoder.dev.controller.request.SingleRequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.controller.selection.requireSelectedItem
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logic.appPreferences.AppPreference
import hardcoder.dev.logic.appPreferences.AppPreferenceUpdater
import hardcoder.dev.logic.currency.WalletCreator
import hardcoder.dev.logic.hero.HeroCreator
import hardcoder.dev.logic.hero.HeroNameValidator
import hardcoder.dev.logic.hero.gender.GenderProvider

class HeroCreationViewModel(
    appPreferenceUpdater: AppPreferenceUpdater,
    genderProvider: GenderProvider,
    heroNameValidator: HeroNameValidator,
    dateTimeProvider: DateTimeProvider,
    heroCreator: HeroCreator,
    walletCreator: WalletCreator,
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

    val nameInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = heroNameValidator::validate,
    )

    val heroCreationController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            val userId = heroCreator.create(
                weight = weightInputController.getInput(),
                exerciseStressTime = exerciseStressTimeInputController.getInput(),
                name = nameInputController.getInput(),
                gender = genderSelectionController.requireSelectedItem(),
            )

            walletCreator.create(
                ownerId = userId,
            )

            appPreferenceUpdater.update(
                AppPreference(
                    firstLaunchTime = dateTimeProvider.currentInstant(),
                    lastAppReviewRequestTime = null,
                    lastEntranceDateTime = dateTimeProvider.currentInstant(),
                ),
            )
        },
    )

    private companion object {
        private const val DEFAULT_WEIGHT = 30
        private const val DEFAULT_EXERCISE_STRESS_TIME = 0
    }
}