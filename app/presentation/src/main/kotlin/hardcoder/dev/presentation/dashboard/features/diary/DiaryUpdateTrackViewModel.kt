package hardcoder.dev.presentation.dashboard.features.diary

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.DiaryTag
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.DiaryTagProvider
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.CorrectValidatedDiaryTrackDescription
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.CorrectValidatedDiaryTrackTitle
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrackDeleter
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrackDescriptionValidator
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrackProvider
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrackTitleValidator
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrackUpdater
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.ValidatedDiaryTrackDescription
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.ValidatedDiaryTrackTitle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DiaryUpdateTrackViewModel(
    private val diaryTrackId: Int,
    private val diaryTrackUpdater: DiaryTrackUpdater,
    private val diaryTrackProvider: DiaryTrackProvider,
    private val diaryTrackDeleter: DiaryTrackDeleter,
    diaryTagProvider: DiaryTagProvider,
    diaryTrackTitleValidator: DiaryTrackTitleValidator,
    diaryTrackDescriptionValidator: DiaryTrackDescriptionValidator
) : ViewModel() {

    private val updateState = MutableStateFlow<UpdateState>(UpdateState.NotExecuted)
    private val deleteState = MutableStateFlow<DeleteState>(DeleteState.NotExecuted)
    private val diaryTrack = MutableStateFlow<Any?>(null)
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
    private val initialTags = diaryTrackProvider.provideDiaryTrackById(diaryTrackId).map { diaryTrack ->
        diaryTrack?.diaryAttachmentGroup?.tags?.let {
            selectedTags.value = it
            it
        } ?: emptyList()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    init {
        fillStateWithUpdatedTrack()
    }

    val state = combine(
        updateState,
        deleteState,
        title,
        validatedTitle,
        description,
        validatedDescription,
        diaryTrack,
        tagList,
        selectedTags
    ) { updateState, deleteState, title, validatedTitle,
        description, validatedDescription, diaryTrack, tagList,
        selectedTagList ->
        State(
            updateAllowed =
            validatedTitle is CorrectValidatedDiaryTrackTitle &&
                    validatedDescription is CorrectValidatedDiaryTrackDescription,
            updateState = updateState,
            deleteState = deleteState,
            title = title,
            validatedTitle = validatedTitle,
            description = description,
            validatedDescription = validatedDescription,
            diaryTrack = diaryTrack,
            tagList = tagList,
            selectedTags = selectedTagList
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            updateAllowed = false,
            updateState = updateState.value,
            deleteState = deleteState.value,
            title = title.value,
            validatedTitle = validatedTitle.value,
            description = description.value,
            validatedDescription = validatedDescription.value,
            diaryTrack = diaryTrack.value,
            tagList = tagList.value,
            selectedTags = initialTags.value
        )
    )

    private fun fillStateWithUpdatedTrack() {
        viewModelScope.launch {
            diaryTrackProvider.provideDiaryTrackById(diaryTrackId).firstOrNull()
                ?.let { track ->
                    val textLastSymbol = track.description.length
                    val titleIfNull = track.title ?: run {
                        if (textLastSymbol > FIRST_SENTENCE_LAST_SYMBOL) {
                            track.description.substring(0, FIRST_SENTENCE_LAST_SYMBOL)
                        } else {
                            track.description.substring(0, textLastSymbol - 1)
                        }
                    }

                    description.value = track.description
                    title.value = track.title ?: titleIfNull
                    diaryTrack.value = track.diaryAttachmentGroup
                        ?.fastingTracks?.firstOrNull()
                        ?: track.diaryAttachmentGroup?.moodTracks?.firstOrNull()
                    selectedTags.value = initialTags.value
                }
        }
    }

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

    fun deleteTrackById() {
        viewModelScope.launch {
            diaryTrackDeleter.deleteById(diaryTrackId)
            deleteState.value = DeleteState.Executed
        }
    }

    fun updateTrack() {
        viewModelScope.launch {
            val validatedTitle = validatedTitle.value
            require(validatedTitle is CorrectValidatedDiaryTrackTitle)

            val validatedDescription = validatedDescription.value
            require(validatedDescription is CorrectValidatedDiaryTrackDescription)

            diaryTrackProvider.provideDiaryTrackById(diaryTrackId).firstOrNull()?.let {
                val updatedTrack = it.copy(
                    description = validatedDescription.data.trim(),
                    title = validatedTitle.data.ifEmpty { null }?.trim()
                )

                diaryTrackUpdater.update(updatedTrack, selectedTags.value)
            } ?: throw Resources.NotFoundException("Track not found by it's id")

            updateState.value = UpdateState.Executed
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
        val title: String?,
        val validatedTitle: ValidatedDiaryTrackTitle?,
        val description: String?,
        val validatedDescription: ValidatedDiaryTrackDescription?,
        val diaryTrack: Any?,
        val tagList: List<DiaryTag>,
        val selectedTags: List<DiaryTag>
    )

    private companion object {
        const val FIRST_SENTENCE_LAST_SYMBOL = 40
    }
}