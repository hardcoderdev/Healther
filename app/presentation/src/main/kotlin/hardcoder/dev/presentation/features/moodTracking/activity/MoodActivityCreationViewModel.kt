package hardcoder.dev.presentation.features.moodTracking.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.input.validateAndRequire
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.controller.selection.requireSelectedItem
import hardcoder.dev.icons.IconResourceProvider
import hardcoder.dev.validators.features.moodTracking.CorrectActivityName
import hardcoder.dev.validators.features.moodTracking.MoodActivityNameValidator
import hardcoder.dev.logics.features.moodTracking.moodActivity.MoodActivityCreator
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