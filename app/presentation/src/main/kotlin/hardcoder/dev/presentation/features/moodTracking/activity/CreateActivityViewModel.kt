package hardcoder.dev.presentation.features.moodTracking.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.logic.features.moodTracking.activity.ActivityCreator
import hardcoder.dev.logic.features.moodTracking.activity.ActivityNameValidator
import hardcoder.dev.logic.features.moodTracking.activity.CorrectActivityName
import hardcoder.dev.logic.features.moodTracking.activity.ValidatedActivityName
import hardcoder.dev.logic.icons.IconResourceProvider
import hardcoder.dev.logic.icons.LocalIcon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CreateActivityViewModel(
    private val activityCreator: ActivityCreator,
    private val activityNameValidator: ActivityNameValidator,
    iconResourceProvider: IconResourceProvider
) : ViewModel() {

    private val creationState = MutableStateFlow<CreationState>(CreationState.NotExecuted)
    private val availableIconsList = MutableStateFlow(iconResourceProvider.getIcons())
    private val activityName = MutableStateFlow<String?>(null)
    private val selectedIcon = MutableStateFlow(iconResourceProvider.getIcon(0))
    private val validatedActivityName = activityName.map {
        it?.let {
            activityNameValidator.validate(it)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    val state = combine(
        creationState,
        activityName,
        availableIconsList,
        selectedIcon,
        validatedActivityName
    ) { creationState, name, availableIconsList, selectedIcon, validatedActivityName ->
        State(
            creationState = creationState,
            name = name,
            validatedName = validatedActivityName,
            availableIconsList = availableIconsList,
            selectedIcon = selectedIcon,
            creationAllowed = validatedActivityName is CorrectActivityName
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            creationAllowed = false,
            creationState = creationState.value,
            name = activityName.value,
            validatedName = validatedActivityName.value,
            availableIconsList = availableIconsList.value,
            selectedIcon = selectedIcon.value
        )
    )

    fun updateActivityName(name: String) {
        activityName.value = name
    }

    fun updateSelectedIcon(icon: LocalIcon) {
        selectedIcon.value = icon
    }

    fun createTrack() {
        viewModelScope.launch {
            val validatedName = validatedActivityName.value
            require(validatedName is ValidatedActivityName)

            activityCreator.create(
                name = validatedName.data,
                icon = selectedIcon.value
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
        val validatedName: ValidatedActivityName?
    )

    sealed class CreationState {
        object Executed : CreationState()
        object NotExecuted : CreationState()
    }
}