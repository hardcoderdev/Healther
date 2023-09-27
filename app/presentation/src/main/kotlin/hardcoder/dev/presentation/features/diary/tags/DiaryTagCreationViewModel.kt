package hardcoder.dev.presentation.features.diary.tags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.input.validateAndRequire
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.controller.selection.requireSelectedItem
import hardcoder.dev.logic.features.diary.diaryTag.CorrectDiaryTagName
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTagCreator
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTagNameValidator
import kotlinx.coroutines.flow.map

class DiaryTagCreationViewModel(
    private val diaryTagCreator: DiaryTagCreator,
    private val diaryTagNameValidator: DiaryTagNameValidator,
    iconResourceProvider: hardcoder.dev.icons.IconResourceProvider,
) : ViewModel() {

    val iconSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        items = iconResourceProvider.getIcons(),
    )

    val nameInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = diaryTagNameValidator::validate,
    )

    val creationController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            diaryTagCreator.create(
                name = nameInputController.validateAndRequire(),
                icon = iconSelectionController.requireSelectedItem(),
            )
        },
        isAllowedFlow = nameInputController.state.map {
            it.validationResult == null || it.validationResult is CorrectDiaryTagName
        },
    )
}