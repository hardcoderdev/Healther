package hardcoder.dev.presentation.features.moodTracking.activity

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.input.validateAndRequire
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.controller.selection.requireSelectedItem
import hardcoder.dev.icons.IconResourceProvider
import hardcoder.dev.logic.features.moodTracking.moodActivity.CorrectActivityName
import hardcoder.dev.logic.features.moodTracking.moodActivity.MoodActivityNameValidator
import hardcoder.dev.logics.features.moodTracking.moodActivity.MoodActivityDeleter
import hardcoder.dev.logics.features.moodTracking.moodActivity.MoodActivityProvider
import hardcoder.dev.logics.features.moodTracking.moodActivity.MoodActivityUpdater
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MoodActivityUpdateViewModel(
    private val activityId: Int,
    private val moodActivityNameValidator: MoodActivityNameValidator,
    private val moodActivityDeleter: MoodActivityDeleter,
    private val moodActivityUpdater: MoodActivityUpdater,
    private val moodActivityProvider: MoodActivityProvider,
    iconResourceProvider: IconResourceProvider,
) : ScreenModel {

    val activityNameController = ValidatedInputController(
        coroutineScope = coroutineScope,
        initialInput = "",
        validation = moodActivityNameValidator::validate,
    )

    val iconSingleSelectionController = SingleSelectionController(
        coroutineScope = coroutineScope,
        items = iconResourceProvider.getIcons(),
    )

    val updateController = RequestController(
        coroutineScope = coroutineScope,
        request = {
            moodActivityUpdater.update(
                id = activityId,
                name = activityNameController.validateAndRequire(),
                icon = iconSingleSelectionController.requireSelectedItem(),
            )
        },
        isAllowedFlow = activityNameController.state.map {
            it.validationResult == null || it.validationResult is CorrectActivityName
        },
    )

    val deleteController = RequestController(
        coroutineScope = coroutineScope,
        request = {
            moodActivityDeleter.deleteById(activityId)
        },
    )

    init {
        coroutineScope.launch {
            moodActivityProvider.provideActivityById(activityId).firstOrNull()?.let { activity ->
                activityNameController.changeInput(activity.name)
                iconSingleSelectionController.select(activity.icon)
            }
        }
    }
}