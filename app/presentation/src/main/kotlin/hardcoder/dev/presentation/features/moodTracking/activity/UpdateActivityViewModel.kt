package hardcoder.dev.presentation.features.moodTracking.activity

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.logic.features.moodTracking.activity.ActivityDeleter
import hardcoder.dev.logic.features.moodTracking.activity.ActivityNameValidator
import hardcoder.dev.logic.features.moodTracking.activity.ActivityProvider
import hardcoder.dev.logic.features.moodTracking.activity.ActivityUpdater
import hardcoder.dev.logic.features.moodTracking.activity.CorrectActivityName
import hardcoder.dev.logic.features.moodTracking.activity.ValidatedActivityName
import hardcoder.dev.logic.icons.IconResourceProvider
import hardcoder.dev.logic.icons.LocalIcon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UpdateActivityViewModel(
    private val activityId: Int,
    private val activityNameValidator: ActivityNameValidator,
    private val activityDeleter: ActivityDeleter,
    private val activityUpdater: ActivityUpdater,
    private val activityProvider: ActivityProvider,
    iconResourceProvider: IconResourceProvider
) : ViewModel() {

    private val updateState = MutableStateFlow<UpdateState>(UpdateState.NotExecuted)
    private val deleteState = MutableStateFlow<DeleteState>(DeleteState.NotExecuted)
    private val activityName = MutableStateFlow<String?>(null)
    private val availableIconResourceList = MutableStateFlow(iconResourceProvider.getIcons())
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

    init {
        fillStateWithUpdatedTrack()
    }

    val state = combine(
        updateState,
        deleteState,
        activityName,
        validatedActivityName,
        availableIconResourceList,
        selectedIcon
    ) { updateState, deleteState, name, validatedActivityName,
        availableIconsList, iconId ->
        State(
            updateState = updateState,
            deleteState = deleteState,
            name = name,
            validatedName = validatedActivityName,
            availableIconsList = availableIconsList,
            selectedIcon = iconId,
            allowUpdate = validatedActivityName is CorrectActivityName
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            updateState = updateState.value,
            deleteState = deleteState.value,
            name = activityName.value,
            validatedName = validatedActivityName.value,
            availableIconsList = availableIconResourceList.value,
            selectedIcon = selectedIcon.value,
            allowUpdate = false
        )
    )

    fun updateActivityName(name: String) {
        activityName.value = name
    }

    fun updateIcon(icon: LocalIcon) {
        selectedIcon.value = icon
    }

    fun updateActivity() {
        viewModelScope.launch {
            val validatedName = validatedActivityName.value
            require(validatedName is CorrectActivityName)

            activityProvider.provideActivityById(activityId).firstOrNull()?.let {
                val updatedTrack = it.copy(
                    id = activityId,
                    name = validatedName.data,
                    icon = selectedIcon.value
                )
                activityUpdater.update(updatedTrack)
                updateState.value = UpdateState.Executed
            } ?: throw Resources.NotFoundException("Activity not found by it's id")
        }
    }

    fun deleteById() {
        viewModelScope.launch {
            activityDeleter.deleteById(activityId)
            deleteState.value = DeleteState.Executed
        }
    }

    private fun fillStateWithUpdatedTrack() {
        viewModelScope.launch {
            selectedIcon.value = availableIconResourceList.first().first()

            activityProvider.provideActivityById(activityId).firstOrNull()?.let { activity ->
                activityName.value = activity.name
                selectedIcon.value = activity.icon
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
        val validatedName: ValidatedActivityName?
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