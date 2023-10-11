package hardcoder.dev.presentation.features.diary.tags

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.input.validateAndRequire
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.controller.selection.requireSelectedItem
import hardcoder.dev.icons.IconResourceProvider
import hardcoder.dev.logic.features.diary.diaryTag.CorrectDiaryTagName
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTagNameValidator
import hardcoder.dev.logics.features.diary.diaryTag.DiaryTagCreator
import kotlinx.coroutines.flow.map

class DiaryTagCreationViewModel(
    private val diaryTagCreator: DiaryTagCreator,
    private val diaryTagNameValidator: DiaryTagNameValidator,
    iconResourceProvider: IconResourceProvider,
) : ScreenModel {

    val iconSelectionController = SingleSelectionController(
        coroutineScope = coroutineScope,
        items = iconResourceProvider.getIcons(),
    )

    val nameInputController = ValidatedInputController(
        coroutineScope = coroutineScope,
        initialInput = "",
        validation = diaryTagNameValidator::validate,
    )

    val creationController = RequestController(
        coroutineScope = coroutineScope,
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