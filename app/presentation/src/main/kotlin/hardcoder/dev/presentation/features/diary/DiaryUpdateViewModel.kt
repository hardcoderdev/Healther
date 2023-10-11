package hardcoder.dev.presentation.features.diary

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.input.validateAndRequire
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.MultiSelectionController
import hardcoder.dev.controller.selection.requireSelectedItems
import hardcoder.dev.controller.selection.selectedItemsOrEmptySet
import hardcoder.dev.coroutines.firstNotNull
import hardcoder.dev.entities.features.diary.DiaryTag
import hardcoder.dev.entities.features.diary.DiaryTrack
import hardcoder.dev.entities.features.moodTracking.MoodTrack
import hardcoder.dev.logic.features.diary.diaryTrack.CorrectDiaryTrackContent
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackContentValidator
import hardcoder.dev.logics.features.diary.diaryTag.DiaryTagProvider
import hardcoder.dev.logics.features.diary.diaryTrack.DiaryTrackDeleter
import hardcoder.dev.logics.features.diary.diaryTrack.DiaryTrackProvider
import hardcoder.dev.logics.features.diary.diaryTrack.DiaryTrackUpdater
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DiaryUpdateViewModel(
    private val diaryTrackId: Int,
    private val diaryTrackUpdater: DiaryTrackUpdater,
    private val diaryTrackProvider: DiaryTrackProvider,
    private val diaryTrackDeleter: DiaryTrackDeleter,
    diaryTagProvider: DiaryTagProvider,
    diaryTrackContentValidator: DiaryTrackContentValidator,
) : ScreenModel {

    private val initialDiaryTrack = MutableStateFlow<DiaryTrack?>(null)

    val diaryAttachmentsLoadingController = LoadingController(
        coroutineScope = coroutineScope,
        flow = initialDiaryTrack.filterNotNull().map { diaryTrack ->
            diaryTrack.diaryAttachmentGroup?.let { diaryAttachmentGroup ->
                ReadOnlyDiaryAttachments(
                    moodTracks = diaryAttachmentGroup.moodTracks,
                    tags = diaryAttachmentGroup.tags,
                )
            } ?: ReadOnlyDiaryAttachments()
        },
    )

    val contentInputController = ValidatedInputController(
        coroutineScope = coroutineScope,
        initialInput = "",
        validation = diaryTrackContentValidator::validate,
    )

    val tagMultiSelectionController = MultiSelectionController(
        coroutineScope = coroutineScope,
        itemsFlow = diaryTagProvider.provideAllDiaryTags(),
    )

    val deleteController = RequestController(
        coroutineScope = coroutineScope,
        request = {
            diaryTrackDeleter.deleteById(diaryTrackId)
        },
    )

    val updateController = RequestController(
        coroutineScope = coroutineScope,
        request = {
            diaryTrackUpdater.update(
                id = diaryTrackId,
                content = contentInputController.validateAndRequire(),
                diaryAttachmentGroup = initialDiaryTrack.firstNotNull().diaryAttachmentGroup?.copy(
                    tags = tagMultiSelectionController.requireSelectedItems(),
                ) ?: hardcoder.dev.entities.features.diary.DiaryAttachmentGroup(
                    tags = tagMultiSelectionController.selectedItemsOrEmptySet().first(),
                ),
            )
        },
        isAllowedFlow = contentInputController.state.map {
            it.validationResult == null || it.validationResult is CorrectDiaryTrackContent
        },
    )

    init {
        coroutineScope.launch {
            val diaryTrack = diaryTrackProvider.provideDiaryTrackById(diaryTrackId).first()!!
            initialDiaryTrack.value = diaryTrack
            tagMultiSelectionController.toggleItems(diaryTrack.diaryAttachmentGroup?.tags?.toList() ?: emptyList())
            contentInputController.changeInput(diaryTrack.content)
        }
    }

    data class ReadOnlyDiaryAttachments(
        val moodTracks: List<MoodTrack> = emptyList(),
        val tags: Set<DiaryTag> = emptySet(),
    ) {
        val isEmpty = moodTracks.isEmpty()
    }
}