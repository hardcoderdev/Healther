package hardcoder.dev.presentation.features.moodTracking.hobby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.logic.features.moodTracking.hobby.CorrectHobbyName
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyCreator
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyNameValidator
import hardcoder.dev.logic.features.moodTracking.hobby.ValidatedHobbyName
import hardcoder.dev.logic.icons.IconResourceProvider
import hardcoder.dev.logic.icons.LocalIcon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HobbyCreateViewModel(
    private val hobbyCreator: HobbyCreator,
    private val hobbyNameValidator: HobbyNameValidator,
    iconResourceProvider: IconResourceProvider
) : ViewModel() {

    private val creationState = MutableStateFlow<CreationState>(CreationState.NotExecuted)
    private val availableIconsList = MutableStateFlow(iconResourceProvider.getIcons())
    private val name = MutableStateFlow<String?>(null)
    private val selectedIcon = MutableStateFlow(iconResourceProvider.getIcon(0))
    private val validatedHobbyName = name.map {
        it?.let {
            hobbyNameValidator.validate(it)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    val state = combine(
        creationState,
        name,
        availableIconsList,
        selectedIcon,
        validatedHobbyName
    ) { creationState, name, availableIconsList, selectedIcon, validatedHobbyName ->
        State(
            creationState = creationState,
            name = name,
            validatedName = validatedHobbyName,
            availableIconsList = availableIconsList,
            selectedIcon = selectedIcon,
            creationAllowed = validatedHobbyName is CorrectHobbyName
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            creationAllowed = false,
            creationState = creationState.value,
            name = name.value,
            validatedName = validatedHobbyName.value,
            availableIconsList = availableIconsList.value,
            selectedIcon = selectedIcon.value
        )
    )

    fun updateHobbyName(name: String) {
        this.name.value = name
    }

    fun updateSelectedIcon(icon: LocalIcon) {
        selectedIcon.value = icon
    }

    fun createTrack() {
        viewModelScope.launch {
            val validatedName = validatedHobbyName.value
            require(validatedName is ValidatedHobbyName)

            hobbyCreator.create(
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
        val validatedName: ValidatedHobbyName?
    )

    sealed class CreationState {
        object Executed : CreationState()
        object NotExecuted : CreationState()
    }
}