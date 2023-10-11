package hardcoder.dev.presentation.features.diary

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.input.validateAndRequire
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.MultiSelectionController
import hardcoder.dev.controller.selection.requireSelectedItems
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logic.features.diary.diaryTrack.CorrectDiaryTrackContent
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackContentValidator
import hardcoder.dev.logics.features.diary.diaryTag.DiaryTagProvider
import hardcoder.dev.logics.features.diary.diaryTrack.DiaryTrackCreator
import kotlinx.coroutines.flow.map

class DiaryCreationViewModel(
    private val diaryTrackCreator: DiaryTrackCreator,
    private val dateTimeProvider: DateTimeProvider,
    diaryTagProvider: DiaryTagProvider,
    diaryTrackContentValidator: DiaryTrackContentValidator,
) : ScreenModel {

    val contentController = ValidatedInputController(
        coroutineScope = coroutineScope,
        initialInput = "",
        validation = diaryTrackContentValidator::validate,
    )

    val tagMultiSelectionController = MultiSelectionController(
        coroutineScope = coroutineScope,
        itemsFlow = diaryTagProvider.provideAllDiaryTags(),
    )

    val creationController = RequestController(
        coroutineScope = coroutineScope,
        request = {
            diaryTrackCreator.create(
                content = contentController.validateAndRequire<CorrectDiaryTrackContent>().data,
                date = dateTimeProvider.currentInstant(),
                diaryAttachmentGroup = hardcoder.dev.entities.features.diary.DiaryAttachmentGroup(
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