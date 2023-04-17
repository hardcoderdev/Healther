package hardcoder.dev.presentation.dashboard.features.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.DiaryTag
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.DiaryTagProvider
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.CorrectValidatedDiaryTrackDescription
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.CorrectValidatedDiaryTrackTitle
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrackCreator
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrackDescriptionValidator
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrackTitleValidator
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.ValidatedDiaryTrackDescription
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.ValidatedDiaryTrackTitle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DiaryCreateTrackViewModel(
    private val diaryTrackCreator: DiaryTrackCreator,
    diaryTagProvider: DiaryTagProvider,
    diaryTrackTitleValidator: DiaryTrackTitleValidator,
    diaryTrackDescriptionValidator: DiaryTrackDescriptionValidator
) : ViewModel() {

    private val creationState = MutableStateFlow<CreationState>(CreationState.NotExecuted)
    private val selectedDate =
        MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
    private val title = MutableStateFlow<String?>(null)
    private val validatedTitle = title.map {
        it?.let {
            diaryTrackTitleValidator.validate(it)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )
    private val description = MutableStateFlow<String?>(null)
    private val validatedDescription = description.map {
        it?.let {
            diaryTrackDescriptionValidator.validate(it)
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
    private var mutableSelectedTags = mutableListOf<DiaryTag>()
    private val selectedTags = MutableStateFlow<List<DiaryTag>>(emptyList())

    val state = combine(
        creationState,
        title,
        validatedTitle,
        description,
        validatedDescription,
        tagList,
        selectedTags
    ) { creationState, title, validatedTitle, description,
        validatedDescription, tagList, selectedTags ->
        State(
            creationAllowed = validatedTitle is CorrectValidatedDiaryTrackTitle &&
                    validatedDescription is CorrectValidatedDiaryTrackDescription,
            creationState = creationState,
            title = title,
            validatedTitle = validatedTitle,
            description = description,
            validatedDiaryTrackDescription = validatedDescription,
            tagList = tagList,
            selectedTags = selectedTags
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            creationAllowed = false,
            creationState = creationState.value,
            title = title.value,
            validatedTitle = validatedTitle.value,
            description = description.value,
            validatedDiaryTrackDescription = validatedDescription.value,
            tagList = tagList.value,
            selectedTags = selectedTags.value
        )
    )

    fun updateText(newText: String) {
        description.value = newText
    }

    fun updateTitle(newTitle: String) {
        title.value = newTitle
    }

    fun addTag(diaryTag: DiaryTag) {
        if (selectedTags.value.contains(diaryTag).not()) {
            mutableSelectedTags = selectedTags.value.toMutableList()
            mutableSelectedTags.add(diaryTag)
            selectedTags.value = mutableSelectedTags
        }
    }

    fun removeTag(diaryTag: DiaryTag) {
        mutableSelectedTags = selectedTags.value.toMutableList()
        mutableSelectedTags.remove(diaryTag)
        selectedTags.value = mutableSelectedTags
    }

    fun createTrack() {
        viewModelScope.launch {
            val validatedTitle = validatedTitle.value
            require(validatedTitle is ValidatedDiaryTrackTitle)

            val validatedDescription = validatedDescription.value
            require(validatedDescription is ValidatedDiaryTrackDescription)

            diaryTrackCreator.create(
                entityId = null,
                attachmentType = null,
                title = validatedTitle.data.trim(),
                description = validatedDescription.data.trim(),
                date = selectedDate.value,
                selectedTags = selectedTags.value
            )

            creationState.value = CreationState.Executed
        }
    }

    data class State(
        val creationAllowed: Boolean,
        val creationState: CreationState,
        val title: String?,
        val validatedTitle: ValidatedDiaryTrackTitle?,
        val description: String?,
        val validatedDiaryTrackDescription: ValidatedDiaryTrackDescription?,
        val tagList: List<DiaryTag>,
        val selectedTags: List<DiaryTag>
    )

    sealed class CreationState {
        object NotExecuted : CreationState()
        object Executed : CreationState()
    }
}