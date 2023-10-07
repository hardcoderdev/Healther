package hardcoder.dev.presentation.features.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.input.validateAndRequire
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.MultiSelectionController
import hardcoder.dev.controller.selection.requireSelectedItems
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logic.features.diary.diaryAttachment.DiaryAttachmentGroup
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTagProvider
import hardcoder.dev.logic.features.diary.diaryTrack.CorrectDiaryTrackContent
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackContentValidator
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackCreator
import kotlinx.coroutines.flow.map

class DiaryCreationViewModel(
    private val diaryTrackCreator: DiaryTrackCreator,
    private val dateTimeProvider: DateTimeProvider,
    diaryTagProvider: DiaryTagProvider,
    diaryTrackContentValidator: DiaryTrackContentValidator,
) : ViewModel() {

    val contentController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = diaryTrackContentValidator::validate,
    )

    val tagMultiSelectionController = MultiSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = diaryTagProvider.provideAllDiaryTags(),
    )

    val creationController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            diaryTrackCreator.create(
                content = contentController.validateAndRequire<CorrectDiaryTrackContent>().data,
                date = dateTimeProvider.currentInstant(),
                diaryAttachmentGroup = DiaryAttachmentGroup(
                    tags = if (tagMultiSelectionController.state.value is MultiSelectionController.State.Empty) {
                        emptySet()
                    } else {
                        tagMultiSelectionController.requireSelectedItems()
                    },
                ),
            )
        },
        isAllowedFlow = contentController.state.map {
            it.validationResult == null || it.validationResult is CorrectDiaryTrackContent
        },
    )
}