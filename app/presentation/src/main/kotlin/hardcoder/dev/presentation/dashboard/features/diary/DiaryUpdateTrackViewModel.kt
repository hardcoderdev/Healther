package hardcoder.dev.presentation.dashboard.features.diary

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.CorrectValidatedDiaryTrackDescription
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrackDeleter
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrackDescriptionValidator
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrackProvider
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrackUpdater
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.ValidatedDiaryTrackDescription
import hardcoder.dev.logic.dashboard.features.diary.diaryWithFeatureType.DiaryWithFeatureTagsProvider
import hardcoder.dev.logic.dashboard.features.diary.featureType.FeatureTag
import hardcoder.dev.logic.dashboard.features.diary.featureType.FeatureTagProvider
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
    featureTagProvider: FeatureTagProvider,
    diaryWithFeatureTagsProvider: DiaryWithFeatureTagsProvider,
    diaryTrackDescriptionValidator: DiaryTrackDescriptionValidator
) : ViewModel() {

    private val updateState = MutableStateFlow<UpdateState>(UpdateState.NotExecuted)
    private val deleteState = MutableStateFlow<DeleteState>(DeleteState.NotExecuted)
    private val title = MutableStateFlow("")
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
    private var mutableSelectedFeatureTags = mutableListOf<FeatureTag>()
    private val selectedFeatureTags = MutableStateFlow<List<FeatureTag>>(emptyList())
    private val initialFeatureTags =
        diaryWithFeatureTagsProvider.provideFeatureTagListByDiaryTrackId(diaryTrackId).map {
            selectedFeatureTags.value = it
            it
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
    private val featureTags = featureTagProvider.provideAllFeatureTags().stateIn(
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
        featureTags,
        title,
        description,
        validatedDescription,
        selectedFeatureTags
    ) { updateState, deleteState, featureTags, title,
        description, validatedDescription, selectedFeatureTags ->
        State(
            updateAllowed = selectedFeatureTags.isNotEmpty(),
            updateState = updateState,
            deleteState = deleteState,
            featureTagList = featureTags,
            selectedFeatureTags = selectedFeatureTags,
            description = description,
            validatedDescription = validatedDescription,
            title = title
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            updateAllowed = false,
            updateState = updateState.value,
            deleteState = deleteState.value,
            featureTagList = featureTags.value,
            selectedFeatureTags = selectedFeatureTags.value,
            description = description.value,
            validatedDescription = validatedDescription.value,
            title = title.value
        )
    )

    private fun fillStateWithUpdatedTrack() {
        viewModelScope.launch {
            diaryTrackProvider.provideDiaryTrackById(diaryTrackId).firstOrNull()
                ?.let { track ->
                    val textLastSymbol = track.text.length
                    val titleIfNull = track.title ?: run {
                        if (textLastSymbol > FIRST_SENTENCE_LAST_SYMBOL) {
                            track.text.substring(0, FIRST_SENTENCE_LAST_SYMBOL)
                        } else {
                            track.text.substring(0, textLastSymbol - 1)
                        }
                    }

                    description.value = track.text
                    title.value = track.title ?: titleIfNull
                    selectedFeatureTags.value = initialFeatureTags.value
                }
        }
    }

    fun updateText(newText: String) {
        description.value = newText
    }

    fun updateTitle(newTitle: String) {
        title.value = newTitle
    }

    fun addFeatureTag(featureTag: FeatureTag) {
        if (selectedFeatureTags.value.contains(featureTag).not()) {
            mutableSelectedFeatureTags = selectedFeatureTags.value.toMutableList()
            mutableSelectedFeatureTags.add(featureTag)
            selectedFeatureTags.value = mutableSelectedFeatureTags
        }
    }

    fun removeFeatureTag(featureTag: FeatureTag) {
        mutableSelectedFeatureTags = selectedFeatureTags.value.toMutableList()
        mutableSelectedFeatureTags.remove(featureTag)
        selectedFeatureTags.value = mutableSelectedFeatureTags
    }

    fun deleteTrackById() {
        viewModelScope.launch {
            diaryTrackDeleter.deleteById(diaryTrackId)
            deleteState.value = DeleteState.Executed
        }
    }

    fun updateTrack() {
        viewModelScope.launch {
            val validatedDescription = validatedDescription.value
            require(validatedDescription is CorrectValidatedDiaryTrackDescription)

            diaryTrackProvider.provideDiaryTrackById(diaryTrackId).firstOrNull()?.let {
                val updatedTrack = it.copy(
                    text = validatedDescription.data,
                    title = title.value.ifEmpty { null }
                )

                diaryTrackUpdater.update(updatedTrack, selectedFeatureTags.value)
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
        val featureTagList: List<FeatureTag>,
        val selectedFeatureTags: List<FeatureTag>,
        val title: String,
        val description: String?,
        val validatedDescription: ValidatedDiaryTrackDescription?
    )

    private companion object {
        const val FIRST_SENTENCE_LAST_SYMBOL = 40
    }
}