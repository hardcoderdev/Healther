package hardcoder.dev.presentation.features.diary.tags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.input.validateAndRequire
import hardcoder.dev.controller.request.SingleRequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.controller.selection.requireSelectedItem
import hardcoder.dev.logic.features.diary.diaryTag.CorrectDiaryTagName
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTagDeleter
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTagNameValidator
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTagProvider
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTagUpdater
import hardcoder.dev.logic.icons.IconResourceProvider
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DiaryTagUpdateViewModel(
    private val tagId: Int,
    private val diaryTagDeleter: DiaryTagDeleter,
    private val diaryTagUpdater: DiaryTagUpdater,
    private val diaryTagProvider: DiaryTagProvider,
    diaryTagNameValidator: DiaryTagNameValidator,
    iconResourceProvider: IconResourceProvider,
) : ViewModel() {

    val tagNameInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = diaryTagNameValidator::validate,
    )

    val iconSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        items = iconResourceProvider.getIcons(),
    )

    val updateController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            diaryTagUpdater.update(
                id = tagId,
                name = tagNameInputController.validateAndRequire(),
                icon = iconSelectionController.requireSelectedItem(),
            )
        },
        isAllowedFlow = tagNameInputController.state.map {
            it.validationResult == null || it.validationResult is CorrectDiaryTagName
        },
    )

    val deleteController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            diaryTagDeleter.deleteById(tagId)
        },
    )

    init {
        viewModelScope.launch {
            diaryTagProvider.provideDiaryTagById(tagId).firstOrNull()?.let { tag ->
                tagNameInputController.changeInput(tag.name)
                iconSelectionController.select(tag.icon)
            }
        }
    }
}