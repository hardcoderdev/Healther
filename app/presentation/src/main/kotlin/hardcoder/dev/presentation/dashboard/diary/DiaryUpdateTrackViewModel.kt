package hardcoder.dev.presentation.dashboard.diary

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.logic.dashboard.features.diary.diaryAttachment.DiaryAttachmentGroup
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.DiaryTag
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.DiaryTagProvider
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.CorrectDiaryTrackContent
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrackDeleter
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrackContentValidator
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrackProvider
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrackUpdater
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.ValidatedDiaryTrackContent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DiaryUpdateTrackViewModel(
    private val diaryTrackId: Int,
    private val diaryTrackUpdater: DiaryTrackUpdater,
    private val diaryTrackProvider: DiaryTrackProvider,
    private val diaryTrackDeleter: DiaryTrackDeleter,
    diaryTagProvider: DiaryTagProvider,
    diaryTrackContentValidator: DiaryTrackContentValidator
) : ViewModel() {

    private val updateState = MutableStateFlow<UpdateState>(UpdateState.NotExecuted)
    private val deleteState = MutableStateFlow<DeleteState>(DeleteState.NotExecuted)
    private val content = MutableStateFlow<String?>(null)
    private val validatedContent = content.map {
        it?.let {
            diaryTrackContentValidator.validate(it)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    private val tagList = diaryTagProvider.provideAllDiaryTags().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    private val diaryAttachmentGroup = MutableStateFlow(DiaryAttachmentGroup())

    init {
        fillStateWithUpdatedTrack()
    }

    val state = combine(
        updateState,
        deleteState,
        content,
        validatedContent,
        tagList,
        diaryAttachmentGroup
    ) { updateState, deleteState, content, validatedContent,
        tagList, diaryAttachmentGroup ->
        State(
            updateAllowed = validatedContent is CorrectDiaryTrackContent,
            updateState = updateState,
            deleteState = deleteState,
            content = content,
            validatedContent = validatedContent,
            tagList = tagList,
            diaryAttachmentGroup = diaryAttachmentGroup
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            updateAllowed = false,
            updateState = updateState.value,
            deleteState = deleteState.value,
            content = content.value,
            validatedContent = validatedContent.value,
            tagList = tagList.value,
            diaryAttachmentGroup = diaryAttachmentGroup.value
        )
    )

    fun updateContent(newText: String) {
        content.value = newText
    }

    fun toggleTag(diaryTag: DiaryTag) {
        val selectedTagMutableList = diaryAttachmentGroup.value.tags.toMutableList()
        val isRemoved = selectedTagMutableList.removeIf { it == diaryTag }
        if (isRemoved) {
            diaryAttachmentGroup.update { previousGroup ->
                previousGroup.copy(tags = selectedTagMutableList)
            }
            return
        } else {
            selectedTagMutableList.add(diaryTag)
            diaryAttachmentGroup.update { previousGroup ->
                previousGroup.copy(tags = selectedTagMutableList)
            }
        }
    }

    fun deleteTrackById() {
        viewModelScope.launch {
            diaryTrackDeleter.deleteById(diaryTrackId)
            deleteState.value = DeleteState.Executed
        }
    }

    fun updateTrack() {
        viewModelScope.launch {
            val validatedContent = validatedContent.value
            require(validatedContent is CorrectDiaryTrackContent)

            diaryTrackProvider.provideDiaryTrackById(diaryTrackId).firstOrNull()?.let {
                val updatedTrack = it.copy(content = validatedContent.data.trim())

                diaryTrackUpdater.update(
                    diaryTrack = updatedTrack,
                    diaryAttachmentGroup = diaryAttachmentGroup.value
                )
            } ?: throw Resources.NotFoundException("Track not found by it's id")

            updateState.value = UpdateState.Executed
        }
    }

    private fun fillStateWithUpdatedTrack() {
        viewModelScope.launch {
            diaryTrackProvider.provideDiaryTrackById(diaryTrackId).first()?.let { track ->
                content.value = track.content
                diaryAttachmentGroup.value = track.diaryAttachmentGroup ?: DiaryAttachmentGroup()
            }
        }
    }

    sealed class UpdateState {
        object NotExecuted : UpdateState()
        object Executed : UpdateState()
    }

    sealed class DeleteState {
        object NotExecuted : DeleteState()
        object Executed : DeleteState()
    }

    data class State(
        val updateAllowed: Boolean,
        val updateState: UpdateState,
        val deleteState: DeleteState,
        val content: String?,
        val validatedContent: ValidatedDiaryTrackContent?,
        val tagList: List<DiaryTag>,
        val diaryAttachmentGroup: DiaryAttachmentGroup
    )
}