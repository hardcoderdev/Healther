package hardcoder.dev.presentation.features.moodTracking.moodType

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.logic.features.moodTracking.moodType.CorrectMoodTypeName
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeCreator
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeNameValidator
import hardcoder.dev.logic.features.moodTracking.moodType.ValidatedMoodTypeName
import hardcoder.dev.logic.icons.IconResourceProvider
import hardcoder.dev.logic.icons.LocalIcon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MoodTypeCreateViewModel(
    private val moodTypeCreator: MoodTypeCreator,
    private val moodTypeNameValidator: MoodTypeNameValidator,
    iconResourceProvider: IconResourceProvider
) : ViewModel() {

    private val creationState = MutableStateFlow<CreationState>(CreationState.NotExecuted)
    private val availableIconResourceList = MutableStateFlow(iconResourceProvider.getIcons())
    private val selectedIcon = MutableStateFlow(iconResourceProvider.getIcon(0))
    private val selectedPositivePercentage = MutableStateFlow(DEFAULT_POSITIVE_PERCENTAGE)
    private val name = MutableStateFlow<String?>(null)
    private val validatedMoodTypeName = name.map {
        it?.let {
            moodTypeNameValidator.validate(it)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    val state = combine(
        creationState,
        name,
        validatedMoodTypeName,
        availableIconResourceList,
        selectedIcon,
        selectedPositivePercentage
    ) { creationState, name, validatedName, availableIcons,
        selectedIcon, selectedPositivePercentage ->
        State(
            creationState = creationState,
            name = name,
            validatedMoodTypeName = validatedName,
            availableIconsList = availableIcons,
            selectedIcon = selectedIcon,
            positivePercentage = selectedPositivePercentage,
            allowCreation = validatedName is CorrectMoodTypeName
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            allowCreation = false,
            creationState = creationState.value,
            name = name.value,
            validatedMoodTypeName = validatedMoodTypeName.value,
            availableIconsList = availableIconResourceList.value,
            selectedIcon = selectedIcon.value,
            positivePercentage = selectedPositivePercentage.value
        )
    )

    fun updateName(newName: String) {
        name.value = newName
    }

    fun updateSelectedIcon(icon: LocalIcon) {
        selectedIcon.value = icon
    }

    fun updatePositivePercentage(positivePercentage: Int) {
        selectedPositivePercentage.value = positivePercentage
    }

    fun createMoodType() {
        viewModelScope.launch {
            val validatedName = validatedMoodTypeName.value
            require(validatedName is CorrectMoodTypeName)

            moodTypeCreator.create(
                name = validatedName.data,
                icon = selectedIcon.value,
                positivePercentage = selectedPositivePercentage.value
            )

            creationState.value = CreationState.Executed
        }
    }

    data class State(
        val allowCreation: Boolean,
        val creationState: CreationState,
        val name: String?,
        val validatedMoodTypeName: ValidatedMoodTypeName?,
        val availableIconsList: List<LocalIcon>,
        val selectedIcon: LocalIcon,
        val positivePercentage: Int
    )

    sealed class CreationState {
        object Executed : CreationState()
        object NotExecuted : CreationState()
    }

    private companion object {
        const val DEFAULT_POSITIVE_PERCENTAGE = 10
    }
}