package hardcoder.dev.presentation.features.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.MultiSelectionController
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.controller.ValidatedInputController
import hardcoder.dev.controller.selectedItemsOrEmptySet
import hardcoder.dev.controller.validateAndRequire
import hardcoder.dev.logic.features.diary.diaryAttachment.DiaryAttachmentGroup
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTagProvider
import hardcoder.dev.logic.features.diary.diaryTrack.CorrectDiaryTrackContent
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackCreator
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackContentValidator
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DiaryCreateTrackViewModel(
    private val diaryTrackCreator: DiaryTrackCreator,
    diaryTagProvider: DiaryTagProvider,
    diaryTrackContentValidator: DiaryTrackContentValidator
) : ViewModel() {

    val dateInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    )

    val contentController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = diaryTrackContentValidator::validate
    )

    val tagMultiSelectionController = MultiSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = diaryTagProvider.provideAllDiaryTags()
    )

    val creationController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            diaryTrackCreator.create(
                content = contentController.validateAndRequire<String>().trim(),
                date = dateInputController.state.value.input,
                diaryAttachmentGroup = DiaryAttachmentGroup(
                    tags = tagMultiSelectionController.selectedItemsOrEmptySet()
                )
            )
        },
        isAllowedFlow = contentController.state.map {
            it.validationResult == null || it.validationResult is CorrectDiaryTrackContent
        }
    )
}