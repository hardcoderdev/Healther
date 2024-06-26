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
import hardcoder.dev.logics.features.moodTracking.moodType.MoodTypeDeleter
import hardcoder.dev.logics.features.moodTracking.moodType.MoodTypeProvider
import hardcoder.dev.logics.features.moodTracking.moodType.MoodTypeUpdater
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MoodTypeUpdateViewModel(
    private val moodTypeId: Int,
    private val moodTypeNameValidator: MoodTypeNameValidator,
    private val moodTypeProvider: MoodTypeProvider,
    private val moodTypeUpdater: MoodTypeUpdater,
    private val moodTypeDeleter: MoodTypeDeleter,
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
        initialInput = 0,
    )

    val updateController = RequestController(
        coroutineScope = coroutineScope,
        request = {
            moodTypeUpdater.update(
                id = moodTypeId,
                name = moodTypeNameController.validateAndRequire(),
                icon = iconSelectionController.requireSelectedItem(),
                positivePercentage = positiveIndexController.getInput(),
            )
        },
        isAllowedFlow = moodTypeNameController.state.map {
            it.validationResult == null || it.validationResult is CorrectMoodTypeName
        },
    )

    val deleteController = RequestController(
        coroutineScope = coroutineScope,
        request = {
            moodTypeDeleter.deleteById(moodTypeId)
        },
    )

    init {
        coroutineScope.launch {
            moodTypeProvider.provideMoodTypeByTrackId(moodTypeId).firstOrNull()?.let { moodType ->
                moodTypeNameController.changeInput(moodType.name)
                iconSelectionController.select(moodType.icon)
                positiveIndexController.changeInput(moodType.positivePercentage)
            }
        }
    }
}