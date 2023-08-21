package hardcoder.dev.presentation.features.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.input.validateAndRequire
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.MultiSelectionController
import hardcoder.dev.controller.selection.requireSelectedItems
import hardcoder.dev.controller.selection.selectedItemsOrEmptySet
import hardcoder.dev.coroutines.firstNotNull
import hardcoder.dev.logic.features.diary.diaryAttachment.DiaryAttachmentGroup
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTag
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTagProvider
import hardcoder.dev.logic.features.diary.diaryTrack.CorrectDiaryTrackContent
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrack
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackContentValidator
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackDeleter
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackProvider
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackUpdater
import hardcoder.dev.logic.features.fasting.track.FastingTrack
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrack
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
) : ViewModel() {

    private val initialDiaryTrack = MutableStateFlow<DiaryTrack?>(null)

    val diaryAttachmentsLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = initialDiaryTrack.filterNotNull().map { diaryTrack ->
            diaryTrack.diaryAttachmentGroup?.let { diaryAttachmentGroup ->
                ReadOnlyDiaryAttachments(
                    fastingTracks = diaryAttachmentGroup.fastingTracks,
                    moodTracks = diaryAttachmentGroup.moodTracks,
                    tags = diaryAttachmentGroup.tags,
                )
            } ?: ReadOnlyDiaryAttachments()
        },
    )

    val contentInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = diaryTrackContentValidator::validate,
    )

    val tagMultiSelectionController = MultiSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = diaryTagProvider.provideAllDiaryTags(),
    )

    val deleteController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            diaryTrackDeleter.deleteById(diaryTrackId)
        },
    )

    val updateController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            diaryTrackUpdater.update(
                id = diaryTrackId,
                content = contentInputController.validateAndRequire(),
                diaryAttachmentGroup = initialDiaryTrack.firstNotNull().diaryAttachmentGroup?.copy(
                    tags = tagMultiSelectionController.requireSelectedItems(),
                ) ?: DiaryAttachmentGroup(
                    tags = tagMultiSelectionController.selectedItemsOrEmptySet().first(),
                ),
            )
        },
        isAllowedFlow = contentInputController.state.map {
            it.validationResult == null || it.validationResult is CorrectDiaryTrackContent
        },
    )

    init {
        viewModelScope.launch {
            val diaryTrack = diaryTrackProvider.provideDiaryTrackById(diaryTrackId).first()!!
            initialDiaryTrack.value = diaryTrack
            tagMultiSelectionController.toggleItems(diaryTrack.diaryAttachmentGroup?.tags?.toList() ?: emptyList())
            contentInputController.changeInput(diaryTrack.content)
        }
    }

    data class ReadOnlyDiaryAttachments(
        val fastingTracks: List<FastingTrack> = emptyList(),
        val moodTracks: List<MoodTrack> = emptyList(),
        val tags: Set<DiaryTag> = emptySet(),
    ) {
        val isEmpty = fastingTracks.isEmpty() && moodTracks.isEmpty()
    }
}