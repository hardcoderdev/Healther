package hardcoder.dev.presentation.features.moodTracking.moodType

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.input.getInput
import hardcoder.dev.controller.input.validateAndRequire
import hardcoder.dev.controller.request.SingleRequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.controller.selection.requireSelectedItem
import hardcoder.dev.logic.features.moodTracking.moodType.CorrectMoodTypeName
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeCreator
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeNameValidator
import hardcoder.dev.logic.icons.IconResourceProvider
import kotlinx.coroutines.flow.map

class MoodTypeCreationViewModel(
    moodTypeCreator: MoodTypeCreator,
    moodTypeNameValidator: MoodTypeNameValidator,
    iconResourceProvider: IconResourceProvider,
) : ViewModel() {

    val moodTypeNameController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = moodTypeNameValidator::validate,
    )

    val iconSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        items = iconResourceProvider.getIcons(),
    )

    val positiveIndexController = InputController(
        coroutineScope = viewModelScope,
        initialInput = DEFAULT_POSITIVE_PERCENTAGE,
    )

    val creationController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            moodTypeCreator.create(
                name = moodTypeNameController.validateAndRequire(),
                icon = iconSelectionController.requireSelectedItem(),
                positiveIndex = positiveIndexController.getInput(),
            )
        },
        isAllowedFlow = moodTypeNameController.state.map {
            it.validationResult == null || it.validationResult is CorrectMoodTypeName
        },
    )

    private companion object {
        const val DEFAULT_POSITIVE_PERCENTAGE = 10
    }
}