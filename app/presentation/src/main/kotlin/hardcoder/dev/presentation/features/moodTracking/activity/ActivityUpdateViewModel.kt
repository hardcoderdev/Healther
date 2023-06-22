package hardcoder.dev.presentation.features.moodTracking.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.controller.ValidatedInputController
import hardcoder.dev.controller.requireSelectedItem
import hardcoder.dev.controller.validateAndRequire
import hardcoder.dev.logic.features.moodTracking.moodActivity.MoodActivityDeleter
import hardcoder.dev.logic.features.moodTracking.moodActivity.MoodActivityNameValidator
import hardcoder.dev.logic.features.moodTracking.moodActivity.MoodActivityProvider
import hardcoder.dev.logic.features.moodTracking.moodActivity.MoodActivityUpdater
import hardcoder.dev.logic.features.moodTracking.moodActivity.CorrectActivityName
import hardcoder.dev.logic.icons.IconResourceProvider
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ActivityUpdateViewModel(
    private val activityId: Int,
    private val moodActivityNameValidator: MoodActivityNameValidator,
    private val moodActivityDeleter: MoodActivityDeleter,
    private val moodActivityUpdater: MoodActivityUpdater,
    private val moodActivityProvider: MoodActivityProvider,
    iconResourceProvider: IconResourceProvider
) : ViewModel() {

    val activityNameController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = moodActivityNameValidator::validate
    )

    val iconSingleSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        items = iconResourceProvider.getIcons()
    )

    val updateController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            moodActivityUpdater.update(
                id = activityId,
                name = activityNameController.validateAndRequire(),
                icon = iconSingleSelectionController.requireSelectedItem()
            )
        },
        isAllowedFlow = activityNameController.state.map {
            it.validationResult == null || it.validationResult is CorrectActivityName
        }
    )

    val deleteController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            moodActivityDeleter.deleteById(activityId)
        }
    )

    init {
        viewModelScope.launch {
            moodActivityProvider.provideActivityById(activityId).firstOrNull()?.let { activity ->
                activityNameController.changeInput(activity.name)
                iconSingleSelectionController.select(activity.icon)
            }
        }
    }
}