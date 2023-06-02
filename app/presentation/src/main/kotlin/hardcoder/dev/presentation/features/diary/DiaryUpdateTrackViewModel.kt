package hardcoder.dev.presentation.features.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.MultiSelectionController
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.controller.ValidatedInputController
import hardcoder.dev.controller.selectedItemsOrEmptySet
import hardcoder.dev.controller.validateAndRequire
import hardcoder.dev.coroutines.firstNotNull
import hardcoder.dev.logic.features.diary.diaryAttachment.DiaryAttachmentGroup
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTag
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTagProvider
import hardcoder.dev.logic.features.diary.diaryTrack.CorrectDiaryTrackContent
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrack
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackDeleter
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackContentValidator
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackProvider
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackUpdater
import hardcoder.dev.logic.features.fasting.track.FastingTrack
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DiaryUpdateTrackViewModel(
    private val diaryTrackId: Int,
    private val diaryTrackUpdater: DiaryTrackUpdater,
    private val diaryTrackProvider: DiaryTrackProvider,
    private val diaryTrackDeleter: DiaryTrackDeleter,
    diaryTagProvider: DiaryTagProvider,
    diaryTrackContentValidator: DiaryTrackContentValidator
) : ViewModel() {

    private val initialDiaryTrack = MutableStateFlow<DiaryTrack?>(null)

    val diaryAttachmentsLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = initialDiaryTrack.filterNotNull().map { diaryTrack ->
            val diaryAttachmentGroup = diaryTrack.diaryAttachmentGroup!!
            ReadOnlyDiaryAttachments(
                moodTracks = diaryAttachmentGroup.moodTracks,
                fastingTracks = diaryAttachmentGroup.fastingTracks,
                tags = diaryAttachmentGroup.tags
            )
        }
    )

    val contentInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = diaryTrackContentValidator::validate
    )

    val tagMultiSelectionController = MultiSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = diaryTagProvider.provideAllDiaryTags()
    )

    val deleteController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            diaryTrackDeleter.deleteById(diaryTrackId)
        }
    )

    val updateController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            diaryTrackUpdater.update(
                id = diaryTrackId,
                content = contentInputController.validateAndRequire(),
                diaryAttachmentGroup = initialDiaryTrack.firstNotNull().diaryAttachmentGroup?.copy(
                    tags = tagMultiSelectionController.selectedItemsOrEmptySet()
                ) ?: DiaryAttachmentGroup()
            )
        },
        isAllowedFlow = contentInputController.state.map {
            it.validationResult == null || it.validationResult is CorrectDiaryTrackContent
        }
    )

    init {
        viewModelScope.launch {
            val diaryTrack = diaryTrackProvider.provideDiaryTrackById(diaryTrackId).first()!!
            initialDiaryTrack.value = diaryTrack
            contentInputController.changeInput(diaryTrack.content)
        }
    }

    data class ReadOnlyDiaryAttachments(
        val moodTracks: List<MoodTrack>,
        val fastingTracks: List<FastingTrack>,
        val tags: Set<DiaryTag>
    ) {
        val isNotEmpty = fastingTracks.isNotEmpty() || moodTracks.isNotEmpty()
    }
}