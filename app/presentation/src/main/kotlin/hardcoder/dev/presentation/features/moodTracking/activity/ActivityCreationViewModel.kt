package hardcoder.dev.presentation.features.moodTracking.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.controller.ValidatedInputController
import hardcoder.dev.controller.requireSelectedItem
import hardcoder.dev.controller.validateAndRequire
import hardcoder.dev.logic.features.moodTracking.moodActivity.MoodActivityCreator
import hardcoder.dev.logic.features.moodTracking.moodActivity.MoodActivityNameValidator
import hardcoder.dev.logic.features.moodTracking.moodActivity.CorrectActivityName
import hardcoder.dev.logic.icons.IconResourceProvider
import kotlinx.coroutines.flow.map

class ActivityCreationViewModel(
    private val moodActivityCreator: MoodActivityCreator,
    moodActivityNameValidator: MoodActivityNameValidator,
    iconResourceProvider: IconResourceProvider
) : ViewModel() {

    val activityNameController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = moodActivityNameValidator::validate
    )

    val iconSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        items = iconResourceProvider.getIcons()
    )

    val creationController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            moodActivityCreator.create(
                name = activityNameController.validateAndRequire(),
                icon = iconSelectionController.requireSelectedItem()
            )
        },
        isAllowedFlow = activityNameController.state.map {
            it.validationResult == null || it.validationResult is CorrectActivityName
        }
    )
}