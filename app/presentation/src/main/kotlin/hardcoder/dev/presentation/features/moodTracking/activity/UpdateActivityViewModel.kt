package hardcoder.dev.presentation.features.moodTracking.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.controller.ValidatedInputController
import hardcoder.dev.controller.requireSelectedItem
import hardcoder.dev.controller.validateAndRequire
import hardcoder.dev.logic.features.moodTracking.activity.ActivityDeleter
import hardcoder.dev.logic.features.moodTracking.activity.ActivityNameValidator
import hardcoder.dev.logic.features.moodTracking.activity.ActivityProvider
import hardcoder.dev.logic.features.moodTracking.activity.ActivityUpdater
import hardcoder.dev.logic.features.moodTracking.activity.CorrectActivityName
import hardcoder.dev.logic.icons.IconResourceProvider
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class UpdateActivityViewModel(
    private val activityId: Int,
    private val activityNameValidator: ActivityNameValidator,
    private val activityDeleter: ActivityDeleter,
    private val activityUpdater: ActivityUpdater,
    private val activityProvider: ActivityProvider,
    iconResourceProvider: IconResourceProvider
) : ViewModel() {

    val activityNameController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = activityNameValidator::validate
    )

    val iconSingleSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        items = iconResourceProvider.getIcons()
    )

    val updateController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            activityUpdater.update(
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
            activityDeleter.deleteById(activityId)
        }
    )

    init {
        viewModelScope.launch {
            activityProvider.provideActivityById(activityId).firstOrNull()?.let { activity ->
                activityNameController.changeInput(activity.name)
                iconSingleSelectionController.select(activity.icon)
            }
        }
    }
}