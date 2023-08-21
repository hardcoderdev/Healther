package hardcoder.dev.presentation.hero

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.input.validateAndRequire
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.controller.selection.requireSelectedItem
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logic.appPreferences.AppPreference
import hardcoder.dev.logic.appPreferences.AppPreferenceUpdater
import hardcoder.dev.logic.hero.CorrectHeroHeroExerciseStress
import hardcoder.dev.logic.hero.CorrectHeroName
import hardcoder.dev.logic.hero.CorrectHeroWeight
import hardcoder.dev.logic.hero.HeroCreator
import hardcoder.dev.logic.hero.HeroExerciseStressValidator
import hardcoder.dev.logic.hero.HeroNameValidator
import hardcoder.dev.logic.hero.HeroWeightValidator
import hardcoder.dev.logic.hero.gender.GenderProvider
import kotlinx.coroutines.flow.combine

class HeroCreationViewModel(
    appPreferenceUpdater: AppPreferenceUpdater,
    genderProvider: GenderProvider,
    heroNameValidator: HeroNameValidator,
    heroWeightValidator: HeroWeightValidator,
    heroExerciseStressValidator: HeroExerciseStressValidator,
    dateTimeProvider: DateTimeProvider,
    heroCreator: HeroCreator,
) : ViewModel() {

    val genderSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = genderProvider.provideAllGenders(),
    )

    val nameInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = heroNameValidator::validate,
    )

    val weightInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = heroWeightValidator::validate,
    )

    val exerciseStressTimeInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = heroExerciseStressValidator::validate,
    )

    val heroCreationController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            heroCreator.create(
                gender = genderSelectionController.requireSelectedItem(),
                weight = weightInputController.validateAndRequire(),
                exerciseStressTime = exerciseStressTimeInputController.validateAndRequire(),
                name = nameInputController.validateAndRequire(),
            )

            appPreferenceUpdater.update(
                AppPreference(
                    firstLaunchTime = dateTimeProvider.currentInstant(),
                    lastAppReviewRequestTime = null,
                    lastEntranceDateTime = dateTimeProvider.currentInstant(),
                ),
            )
        },
        isAllowedFlow = combine(
            nameInputController.state,
            weightInputController.state,
            exerciseStressTimeInputController.state,
        ) { name, weight, exerciseStressTime ->
            name.validationResult is CorrectHeroName &&
                weight.validationResult is CorrectHeroWeight &&
                exerciseStressTime.validationResult is CorrectHeroHeroExerciseStress
        },
    )
}