package hardcoder.dev.presentation.dashboard.features.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.extensions.getStartOfDay
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.CorrectValidatedDiaryTrackDescription
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrackCreator
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrackDescriptionValidator
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.ValidatedDiaryTrackDescription
import hardcoder.dev.logic.dashboard.features.diary.diaryWithFeatureType.DiaryWithFeatureTagsCreator
import hardcoder.dev.logic.dashboard.features.diary.featureType.FeatureTag
import hardcoder.dev.logic.dashboard.features.diary.featureType.FeatureTagProvider
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class DiaryCreateTrackViewModel(
    private val diaryTrackCreator: DiaryTrackCreator,
    private val diaryWithFeatureTagsCreator: DiaryWithFeatureTagsCreator,
    featureTagProvider: FeatureTagProvider,
    diaryTrackDescriptionValidator: DiaryTrackDescriptionValidator
) : ViewModel() {

    private val creationState = MutableStateFlow<CreationState>(CreationState.NotExecuted)
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
    private val title = MutableStateFlow("")
    private val selectedDate = MutableStateFlow(LocalDate.now())
    private var mutableSelectedFeatureTags = mutableListOf<FeatureTag>()
    private val selectedFeatureTags = MutableStateFlow<List<FeatureTag>>(emptyList())
    private val featureTags = featureTagProvider.provideAllFeatureTags().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    val state = combine(
        creationState,
        featureTags,
        title,
        description,
        validatedDescription,
        selectedFeatureTags
    ) { creationState, featureTags, title, description,
        validatedDescription, selectedFeatureTags ->
        State(
            creationAllowed = selectedFeatureTags.isNotEmpty() &&
                    validatedDescription is CorrectValidatedDiaryTrackDescription,
            creationState = creationState,
            featureTagList = featureTags,
            description = description,
            validatedDiaryTrackDescription = validatedDescription,
            title = title,
            selectedFeatureTags = selectedFeatureTags
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            creationAllowed = false,
            creationState = creationState.value,
            featureTagList = featureTags.value,
            selectedFeatureTags = selectedFeatureTags.value,
            description = description.value,
            validatedDiaryTrackDescription = validatedDescription.value,
            title = title.value
        )
    )

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

    fun createTrack() {
        viewModelScope.launch {
            val validatedDescription = validatedDescription.value
            require(validatedDescription is ValidatedDiaryTrackDescription)

            val diaryTrackId = diaryTrackCreator.create(
                date = selectedDate.value.getStartOfDay(),
                text = validatedDescription.data,
                title = title.value.ifEmpty { null }
            )

            selectedFeatureTags.value.forEach { featureTag ->
                diaryWithFeatureTagsCreator.create(
                    diaryTrackId = diaryTrackId,
                    featureTagId = featureTag.id
                )
            }

            creationState.value = CreationState.Executed
        }
    }

    data class State(
        val creationAllowed: Boolean,
        val creationState: CreationState,
        val selectedFeatureTags: List<FeatureTag>,
        val featureTagList: List<FeatureTag>,
        val title: String,
        val description: String?,
        val validatedDiaryTrackDescription: ValidatedDiaryTrackDescription?
    )

    sealed class CreationState {
        object NotExecuted : CreationState()
        object Executed : CreationState()
    }
}