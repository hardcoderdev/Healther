package hardcoder.dev.presentation.dashboard.features.diary.tags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.CorrectValidatedDiaryTagName
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.DiaryTagCreator
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.DiaryTagNameValidator
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.ValidatedDiaryTagName
import hardcoder.dev.logic.icons.IconResourceProvider
import hardcoder.dev.logic.icons.LocalIcon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CreateTagViewModel(
    private val diaryTagCreator: DiaryTagCreator,
    private val diaryTagNameValidator: DiaryTagNameValidator,
    iconResourceProvider: IconResourceProvider
) : ViewModel() {

    private val creationState = MutableStateFlow<CreationState>(CreationState.NotExecuted)
    private val availableIconsList = MutableStateFlow(iconResourceProvider.getIcons())
    private val tagName = MutableStateFlow<String?>(null)
    private val selectedIcon = MutableStateFlow(iconResourceProvider.getIcon(0))
    private val validatedTagName = tagName.map {
        it?.let {
            diaryTagNameValidator.validate(it)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    val state = combine(
        creationState,
        availableIconsList,
        selectedIcon,
        tagName,
        validatedTagName
    ) { creationState, availableIconsList, selectedIcon, tagName,
        validatedTagName ->
        State(
            creationState = creationState,
            creationAllowed = validatedTagName is CorrectValidatedDiaryTagName,
            availableIconsList = availableIconsList,
            selectedIcon = selectedIcon,
            name = tagName,
            validatedName = validatedTagName
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            creationState = creationState.value,
            creationAllowed = false,
            availableIconsList = availableIconsList.value,
            selectedIcon = selectedIcon.value,
            name = tagName.value,
            validatedName = validatedTagName.value
        )
    )

    fun updateTagName(name: String) {
        tagName.value = name
    }

    fun updateSelectedIcon(icon: LocalIcon) {
        selectedIcon.value = icon
    }

    fun createTag() {
        viewModelScope.launch {
            val validatedName = validatedTagName.value
            require(validatedName is CorrectValidatedDiaryTagName)

            diaryTagCreator.create(
                name = validatedName.data,
                iconId = selectedIcon.value.id
            )

            creationState.value = CreationState.Executed
        }
    }

    data class State(
        val creationState: CreationState,
        val creationAllowed: Boolean,
        val name: String?,
        val availableIconsList: List<LocalIcon>,
        val selectedIcon: LocalIcon,
        val validatedName: ValidatedDiaryTagName?
    )

    sealed class CreationState {
        object Executed : CreationState()
        object NotExecuted : CreationState()
    }
}