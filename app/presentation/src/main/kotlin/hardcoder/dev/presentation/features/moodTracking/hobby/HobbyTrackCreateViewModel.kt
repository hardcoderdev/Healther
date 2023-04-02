package hardcoder.dev.presentation.features.moodTracking.hobby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.logic.features.moodTracking.hobby.CorrectHobbyIcon
import hardcoder.dev.logic.features.moodTracking.hobby.CorrectHobbyName
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyIconResource
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyIconValidator
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyName
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyNameValidator
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyTrackCreator
import hardcoder.dev.logic.features.moodTracking.hobby.ValidateHobbyIcon
import hardcoder.dev.logic.features.moodTracking.hobby.ValidatedHobbyName
import hardcoder.dev.logic.icons.IconResourceProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HobbyTrackCreateViewModel(
    private val hobbyTrackCreator: HobbyTrackCreator,
    private val hobbyNameValidator: HobbyNameValidator,
    private val hobbyIconValidator: HobbyIconValidator,
    iconResourceProvider: IconResourceProvider
) : ViewModel() {

    private val creationState = MutableStateFlow<CreationState>(CreationState.NotExecuted)
    private val availableIconResourceList =
        MutableStateFlow(iconResourceProvider.provideMoodTrackingHobbyIconResources())
    private val name = MutableStateFlow<String?>(null)
    private val selectedIconResource = MutableStateFlow<String?>(null)
    private val validatedHobbyName = name.map {
        it?.let {
            hobbyNameValidator.validate(HobbyName(it))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    private val validatedIconResource = selectedIconResource.map {
        it?.let {
            hobbyIconValidator.validate(HobbyIconResource(it))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    init {
        viewModelScope.launch {
            selectedIconResource.value = availableIconResourceList.first().first()
        }
    }

    val state = combine(
        creationState,
        name,
        availableIconResourceList,
        selectedIconResource,
        validatedHobbyName,
        validatedIconResource
    ) { creationState, name, availableIconResourceList, selectedIconResource,
        validatedHobbyName, validatedHobbyIcon ->
        State(
            creationState = creationState,
            name = name,
            validatedName = validatedHobbyName,
            availableIconResourceList = availableIconResourceList,
            selectedIconResource = selectedIconResource,
            validatedIconResource = validatedHobbyIcon,
            creationAllowed = validatedHobbyName is CorrectHobbyName
                    && validatedHobbyIcon is CorrectHobbyIcon
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            creationAllowed = false,
            creationState = creationState.value,
            name = name.value,
            validatedName = validatedHobbyName.value,
            availableIconResourceList = availableIconResourceList.value,
            selectedIconResource = selectedIconResource.value,
            validatedIconResource = validatedIconResource.value
        )
    )

    fun updateHobbyName(name: String) {
        this.name.value = name
    }

    fun updateHobbyIconResId(iconResourceName: String) {
        selectedIconResource.value = iconResourceName
    }

    fun createTrack() {
        viewModelScope.launch {
            val validatedName = validatedHobbyName.value
            require(validatedName is ValidatedHobbyName)

            val validatedIconResource = validatedIconResource.value
            require(validatedIconResource is ValidateHobbyIcon)

            hobbyTrackCreator.create(
                name = validatedName.data.value,
                iconResourceName = validatedIconResource.data.value
            )

            creationState.value = CreationState.Executed
        }
    }

    data class State(
        val creationState: CreationState,
        val creationAllowed: Boolean,
        val name: String?,
        val availableIconResourceList: List<String>,
        val selectedIconResource: String?,
        val validatedName: ValidatedHobbyName?,
        val validatedIconResource: ValidateHobbyIcon?
    )

    sealed class CreationState {
        object Executed : CreationState()
        object NotExecuted : CreationState()
    }
}