package hardcoder.dev.presentation.features.moodTracking.activity

import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.input.validateAndRequire
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.controller.selection.requireSelectedItem
import hardcoder.dev.icons.IconResourceProvider
import hardcoder.dev.logic.features.moodTracking.moodActivity.CorrectActivityName
import hardcoder.dev.logics.features.moodTracking.moodActivity.MoodActivityCreator
import hardcoder.dev.logic.features.moodTracking.moodActivity.MoodActivityNameValidator
import hardcoder.dev.viewmodel.ViewModel
import kotlinx.coroutines.flow.map

class MoodActivityCreationViewModel(
    private val moodActivityCreator: MoodActivityCreator,
    moodActivityNameValidator: MoodActivityNameValidator,
    iconResourceProvider: IconResourceProvider,
) : ViewModel() {

    val activityNameController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = moodActivityNameValidator::validate,
    )

    val iconSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        items = iconResourceProvider.getIcons(),
    )

    val creationController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            moodActivityCreator.create(
                name = activityNameController.validateAndRequire(),
                icon = iconSelectionController.requireSelectedItem(),
            )
        },
        isAllowedFlow = activityNameController.state.map {
            it.validationResult == null || it.validationResult is CorrectActivityName
        },
    )
}