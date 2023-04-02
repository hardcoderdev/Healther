package hardcoder.dev.presentation.features.moodTracking.hobby

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.logic.features.moodTracking.hobby.CorrectHobbyIcon
import hardcoder.dev.logic.features.moodTracking.hobby.CorrectHobbyName
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyIconResource
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyIconValidator
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyName
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyNameValidator
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyTrackDeleter
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyTrackProvider
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyTrackUpdater
import hardcoder.dev.logic.features.moodTracking.hobby.ValidateHobbyIcon
import hardcoder.dev.logic.features.moodTracking.hobby.ValidatedHobbyName
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackDeleter
import hardcoder.dev.logic.icons.IconResourceProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HobbyTrackUpdateViewModel(
    private val hobbyTrackId: Int,
    private val hobbyNameValidator: HobbyNameValidator,
    private val hobbyIconValidator: HobbyIconValidator,
    private val moodTrackDeleter: MoodTrackDeleter,
    private val hobbyTrackDeleter: HobbyTrackDeleter,
    private val hobbyTrackUpdater: HobbyTrackUpdater,
    private val hobbyTrackProvider: HobbyTrackProvider,
    iconResourceProvider: IconResourceProvider
) : ViewModel() {

    private val updateState = MutableStateFlow<UpdateState>(UpdateState.NotExecuted)
    private val deleteState = MutableStateFlow<DeleteState>(DeleteState.NotExecuted)
    private val name = MutableStateFlow<String?>(null)
    private val availableIconResourceList =
        MutableStateFlow(iconResourceProvider.provideMoodTrackingHobbyIconResources())
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
    private val validatedHobbyIconResource = selectedIconResource.map {
        it?.let {
            hobbyIconValidator.validate(HobbyIconResource(it))
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
        selectedIconResource,
        validatedHobbyIconResource
    ) { updateState, deleteState, name, validatedHobbyName,
        availableIconResourceList, iconResourceName, validatedIconResourceName ->
        State(
            updateState = updateState,
            deleteState = deleteState,
            name = name,
            validatedName = validatedHobbyName,
            availableIconResourceList = availableIconResourceList,
            selectedIconResource = iconResourceName,
            validatedIconResource = validatedIconResourceName,
            allowUpdate = validatedHobbyName is CorrectHobbyName
                    && validatedIconResourceName is CorrectHobbyIcon
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            updateState = updateState.value,
            deleteState = deleteState.value,
            name = name.value,
            validatedName = validatedHobbyName.value,
            availableIconResourceList = availableIconResourceList.value,
            selectedIconResource = selectedIconResource.value,
            validatedIconResource = validatedHobbyIconResource.value,
            allowUpdate = false
        )
    )

    fun updateHobbyName(name: String) {
        this.name.value = name
    }

    fun updateHobbyIconResId(iconResourceName: String) {
        selectedIconResource.value = iconResourceName
    }

    fun updateHobbyTrack() {
        viewModelScope.launch {
            val validatedName = validatedHobbyName.value
            require(validatedName is CorrectHobbyName)

            val validatedHobbyIcon = validatedHobbyIconResource.value
            require(validatedHobbyIcon is ValidateHobbyIcon)

            hobbyTrackProvider.provideHobbyById(hobbyTrackId).firstOrNull()?.let {
                val updatedTrack = it.copy(
                    id = hobbyTrackId,
                    name = validatedName.data.value,
                    iconResourceName = validatedHobbyIcon.data.value
                )
                hobbyTrackUpdater.update(updatedTrack)
                updateState.value = UpdateState.Executed
            } ?: throw Resources.NotFoundException("Track not found by it's id")
        }
    }

    fun deleteById() {
        viewModelScope.launch {
            hobbyTrackDeleter.deleteById(hobbyTrackId)
            deleteState.value = DeleteState.Executed
        }
    }

    private fun fillStateWithUpdatedTrack() {
        viewModelScope.launch {
            selectedIconResource.value = availableIconResourceList.first().first()

            hobbyTrackProvider.provideHobbyById(hobbyTrackId).firstOrNull()?.let { hobbyTrack ->
                name.value = hobbyTrack.name
                selectedIconResource.value = hobbyTrack.iconResourceName
            }
        }
    }

    data class State(
        val updateState: UpdateState,
        val deleteState: DeleteState,
        val allowUpdate: Boolean,
        val name: String?,
        val availableIconResourceList: List<String>,
        val selectedIconResource: String?,
        val validatedName: ValidatedHobbyName?,
        val validatedIconResource: ValidateHobbyIcon?
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