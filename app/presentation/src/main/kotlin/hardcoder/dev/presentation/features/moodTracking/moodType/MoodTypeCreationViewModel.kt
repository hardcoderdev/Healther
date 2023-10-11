package hardcoder.dev.presentation.features.moodTracking.moodType

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.input.getInput
import hardcoder.dev.controller.input.validateAndRequire
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.controller.selection.requireSelectedItem
import hardcoder.dev.icons.IconResourceProvider
import hardcoder.dev.logic.features.moodTracking.moodType.CorrectMoodTypeName
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeNameValidator
import hardcoder.dev.logics.features.moodTracking.moodType.MoodTypeCreator
import kotlinx.coroutines.flow.map

class MoodTypeCreationViewModel(
    moodTypeCreator: MoodTypeCreator,
    moodTypeNameValidator: MoodTypeNameValidator,
    iconResourceProvider: IconResourceProvider,
) : ScreenModel {

    val moodTypeNameController = ValidatedInputController(
        coroutineScope = coroutineScope,
        initialInput = "",
        validation = moodTypeNameValidator::validate,
    )

    val iconSelectionController = SingleSelectionController(
        coroutineScope = coroutineScope,
        items = iconResourceProvider.getIcons(),
    )

    val positiveIndexController = InputController(
        coroutineScope = coroutineScope,
        initialInput = DEFAULT_POSITIVE_PERCENTAGE,
    )

    val creationController = RequestController(
        coroutineScope = coroutineScope,
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