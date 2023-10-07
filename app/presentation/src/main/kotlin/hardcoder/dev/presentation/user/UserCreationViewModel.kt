package hardcoder.dev.presentation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.input.validateAndRequire
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.controller.selection.requireSelectedItem
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logic.appPreferences.AppPreferenceUpdater
import hardcoder.dev.logic.user.UserCreator
import hardcoder.dev.logic.user.UserGenderProvider
import hardcoder.dev.validators.user.CorrectUserExerciseStressTime
import hardcoder.dev.validators.user.CorrectUserName
import hardcoder.dev.validators.user.CorrectUserWeight
import hardcoder.dev.validators.user.UserExerciseStressValidator
import hardcoder.dev.validators.user.UserNameValidator
import hardcoder.dev.validators.user.UserWeightValidator
import kotlinx.coroutines.flow.combine

class UserCreationViewModel(
    appPreferenceUpdater: AppPreferenceUpdater,
    userGenderProvider: UserGenderProvider,
    userNameValidator: UserNameValidator,
    userWeightValidator: UserWeightValidator,
    userExerciseStressValidator: UserExerciseStressValidator,
    dateTimeProvider: DateTimeProvider,
    userCreator: UserCreator,
) : ViewModel() {

    val genderSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = userGenderProvider.provideAllGenders(),
    )

    val nameInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = userNameValidator::validate,
    )

    val weightInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = userWeightValidator::validate,
    )

    val exerciseStressTimeInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = userExerciseStressValidator::validate,
    )

    val userCreationController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            userCreator.create(
                gender = genderSelectionController.requireSelectedItem(),
                weight = weightInputController.validateAndRequire(),
                exerciseStressTime = exerciseStressTimeInputController.validateAndRequire(),
                name = nameInputController.validateAndRequire(),
            )

            appPreferenceUpdater.update(
                hardcoder.dev.entities.appPreferences.AppPreference(
                    firstLaunchTime = dateTimeProvider.currentInstant(),
                ),
            )
        },
        isAllowedFlow = combine(
            nameInputController.state,
            weightInputController.state,
            exerciseStressTimeInputController.state,
        ) { name, weight, exerciseStressTime ->
            name.validationResult is CorrectUserName &&
                weight.validationResult is CorrectUserWeight &&
                exerciseStressTime.validationResult is CorrectUserExerciseStressTime
        },
    )
}