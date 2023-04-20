package hardcoder.dev.presentation.dashboard.features.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.logic.dashboard.features.diary.diaryAttachment.DiaryAttachmentGroup
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.DiaryTag
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.DiaryTagProvider
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.CorrectDiaryTrackContent
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrackCreator
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrackContentValidator
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.ValidatedDiaryTrackContent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DiaryCreateTrackViewModel(
    private val diaryTrackCreator: DiaryTrackCreator,
    diaryTagProvider: DiaryTagProvider,
    diaryTrackContentValidator: DiaryTrackContentValidator
) : ViewModel() {

    private val creationState = MutableStateFlow<CreationState>(CreationState.NotExecuted)
    private val selectedDate =
        MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
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

    private val selectedTagList = MutableStateFlow<List<DiaryTag>>(emptyList())
    private val tagList = diaryTagProvider.provideAllDiaryTags().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    val state = combine(
        creationState,
        content,
        validatedContent,
        tagList,
        selectedTagList
    ) { creationState, content, validatedContent, tagList,
        selectedTags ->
        State(
            creationAllowed = validatedContent is CorrectDiaryTrackContent,
            creationState = creationState,
            description = content,
            validatedDiaryTrackContent = validatedContent,
            tagList = tagList,
            selectedTags = selectedTags
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            creationAllowed = false,
            creationState = creationState.value,
            description = content.value,
            validatedDiaryTrackContent = validatedContent.value,
            tagList = tagList.value,
            selectedTags = selectedTagList.value
        )
    )

    fun updateContent(newText: String) {
        content.value = newText
    }

    fun toggleTag(diaryTag: DiaryTag) {
        val selectedTagMutableList = selectedTagList.value.toMutableList()
        val isRemoved = selectedTagMutableList.removeIf { it == diaryTag }
        if (isRemoved) {
            selectedTagList.value = selectedTagMutableList
            return
        } else {
            selectedTagMutableList.add(diaryTag)
            selectedTagList.value = selectedTagMutableList
        }
    }

    fun createTrack() {
        viewModelScope.launch {
            val validatedContent = validatedContent.value
            require(validatedContent is ValidatedDiaryTrackContent)

            diaryTrackCreator.create(
                content = validatedContent.data.trim(),
                date = selectedDate.value,
                diaryAttachmentGroup = DiaryAttachmentGroup(
                    tags = selectedTagList.value
                )
            )

            creationState.value = CreationState.Executed
        }
    }

    data class State(
        val creationAllowed: Boolean,
        val creationState: CreationState,
        val description: String?,
        val validatedDiaryTrackContent: ValidatedDiaryTrackContent?,
        val tagList: List<DiaryTag>,
        val selectedTags: List<DiaryTag>
    )

    sealed class CreationState {
        object NotExecuted : CreationState()
        object Executed : CreationState()
    }
}