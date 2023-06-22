package hardcoder.dev.presentation.features.moodTracking.moodType

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.controller.ValidatedInputController
import hardcoder.dev.controller.requireSelectedItem
import hardcoder.dev.controller.validateAndRequire
import hardcoder.dev.logic.features.moodTracking.moodType.CorrectMoodTypeName
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeCreator
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeNameValidator
import hardcoder.dev.logic.icons.IconResourceProvider
import kotlinx.coroutines.flow.map

class MoodTypeCreationViewModel(
    moodTypeCreator: MoodTypeCreator,
    moodTypeNameValidator: MoodTypeNameValidator,
    iconResourceProvider: IconResourceProvider
) : ViewModel() {

    val moodTypeNameController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = moodTypeNameValidator::validate
    )

    val iconSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        items = iconResourceProvider.getIcons()
    )

    val positiveIndexController = InputController(
        coroutineScope = viewModelScope,
        initialInput = DEFAULT_POSITIVE_PERCENTAGE
    )

    val creationController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            moodTypeCreator.create(
                name = moodTypeNameController.validateAndRequire(),
                icon = iconSelectionController.requireSelectedItem(),
                positiveIndex = positiveIndexController.state.value.input
            )
        },
        isAllowedFlow = moodTypeNameController.state.map {
            it.validationResult == null || it.validationResult is CorrectMoodTypeName
        }
    )

    private companion object {
        const val DEFAULT_POSITIVE_PERCENTAGE = 10
    }
}