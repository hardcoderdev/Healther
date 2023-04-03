package hardcoder.dev.presentation.features.moodTracking.moodType

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.logic.features.moodTracking.moodType.CorrectValidatedMoodTypeName
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeDeleter
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeNameValidator
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeProvider
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeUpdater
import hardcoder.dev.logic.features.moodTracking.moodType.ValidatedMoodTypeName
import hardcoder.dev.logic.icons.IconResourceProvider
import hardcoder.dev.logic.icons.LocalIcon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MoodTypeUpdateViewModel(
    private val moodTypeId: Int,
    private val moodTypeNameValidator: MoodTypeNameValidator,
    private val moodTypeProvider: MoodTypeProvider,
    private val moodTypeUpdater: MoodTypeUpdater,
    private val moodTypeDeleter: MoodTypeDeleter,
    iconResourceProvider: IconResourceProvider
) : ViewModel() {

    private val updateState = MutableStateFlow<UpdateState>(UpdateState.NotExecuted)
    private val deleteState = MutableStateFlow<DeleteState>(DeleteState.NotExecuted)
    private val availableIconResourceList = MutableStateFlow(iconResourceProvider.getIcons())
    private val selectedIcon = MutableStateFlow(iconResourceProvider.getIcon(0))
    private val selectedPositivePercentage = MutableStateFlow(0)
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

    init {
        fillStateWithUpdatedTrack()
    }

    val state = combine(
        updateState,
        deleteState,
        name,
        validatedMoodTypeName,
        availableIconResourceList,
        selectedIcon,
        selectedPositivePercentage
    ) { updateState, deleteState, name, validatedName,
        availableIconResourceList, selectedIcon, selectedPositivePercentage ->
        State(
            updateState = updateState,
            deleteState = deleteState,
            name = name,
            validatedMoodTypeName = validatedName,
            availableIconsList = availableIconResourceList,
            selectedIcon = selectedIcon,
            positivePercentage = selectedPositivePercentage,
            allowUpdate = validatedName is CorrectValidatedMoodTypeName
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            updateState = updateState.value,
            deleteState = deleteState.value,
            name = name.value,
            validatedMoodTypeName = validatedMoodTypeName.value,
            availableIconsList = availableIconResourceList.value,
            selectedIcon = selectedIcon.value,
            positivePercentage = selectedPositivePercentage.value,
            allowUpdate = false
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

    fun updateMoodType() {
        viewModelScope.launch {
            val validatedName = validatedMoodTypeName.value
            require(validatedName is CorrectValidatedMoodTypeName)

            moodTypeProvider.provideMoodTypeByTrackId(moodTypeId).firstOrNull()?.let {
                val updatedTrack = it.copy(
                    id = moodTypeId,
                    name = validatedName.data,
                    icon = selectedIcon.value,
                    positivePercentage = selectedPositivePercentage.value
                )
                moodTypeUpdater.update(updatedTrack)
                updateState.value = UpdateState.Executed
            } ?: throw Resources.NotFoundException("Track not found by it's id")
        }
    }

    fun deleteById() {
        viewModelScope.launch {
            moodTypeDeleter.deleteById(moodTypeId)
            deleteState.value = DeleteState.Executed
        }
    }

    private fun fillStateWithUpdatedTrack() {
        viewModelScope.launch {
            moodTypeProvider.provideMoodTypeByTrackId(moodTypeId).firstOrNull()?.let { moodType ->
                name.value = moodType.name
                selectedIcon.value = moodType.icon
                selectedPositivePercentage.value = moodType.positivePercentage
            }
        }
    }

    data class State(
        val updateState: UpdateState,
        val deleteState: DeleteState,
        val allowUpdate: Boolean,
        val name: String?,
        val validatedMoodTypeName: ValidatedMoodTypeName?,
        val availableIconsList: List<LocalIcon>,
        val selectedIcon: LocalIcon,
        val positivePercentage: Int
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