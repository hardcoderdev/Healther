package hardcoder.dev.presentation.features.moodTracking.hobby

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.logic.features.moodTracking.hobby.CorrectHobbyName
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyDeleter
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyNameValidator
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyProvider
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyUpdater
import hardcoder.dev.logic.features.moodTracking.hobby.ValidatedHobbyName
import hardcoder.dev.logic.icons.IconResourceProvider
import hardcoder.dev.logic.icons.LocalIcon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HobbyUpdateViewModel(
    private val hobbyTrackId: Int,
    private val hobbyNameValidator: HobbyNameValidator,
    private val hobbyDeleter: HobbyDeleter,
    private val hobbyUpdater: HobbyUpdater,
    private val hobbyProvider: HobbyProvider,
    iconResourceProvider: IconResourceProvider
) : ViewModel() {

    private val updateState = MutableStateFlow<UpdateState>(UpdateState.NotExecuted)
    private val deleteState = MutableStateFlow<DeleteState>(DeleteState.NotExecuted)
    private val name = MutableStateFlow<String?>(null)
    private val availableIconResourceList = MutableStateFlow(iconResourceProvider.getIcons())
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

    init {
        fillStateWithUpdatedTrack()
    }

    val state = combine(
        updateState,
        deleteState,
        name,
        validatedHobbyName,
        availableIconResourceList,
        selectedIcon
    ) { updateState, deleteState, name, validatedHobbyName,
        availableIconsList, iconId ->
        State(
            updateState = updateState,
            deleteState = deleteState,
            name = name,
            validatedName = validatedHobbyName,
            availableIconsList = availableIconsList,
            selectedIcon = iconId,
            allowUpdate = validatedHobbyName is CorrectHobbyName
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            updateState = updateState.value,
            deleteState = deleteState.value,
            name = name.value,
            validatedName = validatedHobbyName.value,
            availableIconsList = availableIconResourceList.value,
            selectedIcon = selectedIcon.value,
            allowUpdate = false
        )
    )

    fun updateHobbyName(name: String) {
        this.name.value = name
    }

    fun updateIcon(icon: LocalIcon) {
        selectedIcon.value = icon
    }

    fun updateHobbyTrack() {
        viewModelScope.launch {
            val validatedName = validatedHobbyName.value
            require(validatedName is CorrectHobbyName)

            hobbyProvider.provideHobbyById(hobbyTrackId).firstOrNull()?.let {
                val updatedTrack = it.copy(
                    id = hobbyTrackId,
                    name = validatedName.data,
                    icon = selectedIcon.value
                )
                hobbyUpdater.update(updatedTrack)
                updateState.value = UpdateState.Executed
            } ?: throw Resources.NotFoundException("Track not found by it's id")
        }
    }

    fun deleteById() {
        viewModelScope.launch {
            hobbyDeleter.deleteById(hobbyTrackId)
            deleteState.value = DeleteState.Executed
        }
    }

    private fun fillStateWithUpdatedTrack() {
        viewModelScope.launch {
            selectedIcon.value = availableIconResourceList.first().first()

            hobbyProvider.provideHobbyById(hobbyTrackId).firstOrNull()?.let { hobbyTrack ->
                name.value = hobbyTrack.name
                selectedIcon.value = hobbyTrack.icon
            }
        }
    }

    data class State(
        val updateState: UpdateState,
        val deleteState: DeleteState,
        val allowUpdate: Boolean,
        val name: String?,
        val availableIconsList: List<LocalIcon>,
        val selectedIcon: LocalIcon,
        val validatedName: ValidatedHobbyName?
    )

    sealed class UpdateState {
        object Executed : UpdateState()
        object NotExecuted : UpdateState()
    }

    sealed class DeleteState {
        object Executed : DeleteState()
        object NotExecuted : DeleteState()
    }
}