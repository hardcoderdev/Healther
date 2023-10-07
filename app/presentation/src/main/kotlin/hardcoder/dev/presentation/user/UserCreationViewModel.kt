package hardcoder.dev.presentation.user

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
import hardcoder.dev.logic.user.CorrectUserExerciseStressTime
import hardcoder.dev.logic.user.CorrectUserName
import hardcoder.dev.logic.user.CorrectUserWeight
import hardcoder.dev.logic.user.UserCreator
import hardcoder.dev.logic.user.UserExerciseStressValidator
import hardcoder.dev.logic.user.UserNameValidator
import hardcoder.dev.logic.user.UserWeightValidator
import hardcoder.dev.logic.user.gender.GenderProvider
import kotlinx.coroutines.flow.combine

class UserCreationViewModel(
    appPreferenceUpdater: AppPreferenceUpdater,
    genderProvider: GenderProvider,
    userNameValidator: UserNameValidator,
    userWeightValidator: UserWeightValidator,
    userExerciseStressValidator: UserExerciseStressValidator,
    dateTimeProvider: DateTimeProvider,
    userCreator: UserCreator,
) : ViewModel() {

    val genderSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = genderProvider.provideAllGenders(),
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
                AppPreference(
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