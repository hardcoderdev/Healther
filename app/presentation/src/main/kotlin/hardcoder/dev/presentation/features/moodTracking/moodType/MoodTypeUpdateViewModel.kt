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
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeDeleter
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeNameValidator
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeProvider
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeUpdater
import hardcoder.dev.logic.icons.IconResourceProvider
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MoodTypeUpdateViewModel(
    private val moodTypeId: Int,
    private val moodTypeNameValidator: MoodTypeNameValidator,
    private val moodTypeProvider: MoodTypeProvider,
    private val moodTypeUpdater: MoodTypeUpdater,
    private val moodTypeDeleter: MoodTypeDeleter,
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
        initialInput = 0
    )

    val updateController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            moodTypeUpdater.update(
                id = moodTypeId,
                name = moodTypeNameController.validateAndRequire(),
                icon = iconSelectionController.requireSelectedItem(),
                positivePercentage = positiveIndexController.state.value.input
            )
        },
        isAllowedFlow = moodTypeNameController.state.map {
            it.validationResult == null || it.validationResult is CorrectMoodTypeName
        }
    )

    val deleteController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            moodTypeDeleter.deleteById(moodTypeId)
        }
    )

    init {
        viewModelScope.launch {
            moodTypeProvider.provideMoodTypeByTrackId(moodTypeId).firstOrNull()?.let { moodType ->
                moodTypeNameController.changeInput(moodType.name)
                iconSelectionController.select(moodType.icon)
                positiveIndexController.changeInput(moodType.positivePercentage)
            }
        }
    }
}