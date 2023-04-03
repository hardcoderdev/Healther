package hardcoder.dev.presentation.features.waterTracking.drinkType

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.logic.features.waterTracking.drinkType.CorrectValidatedDrinkTypeName
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeDeleter
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeNameValidator
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeProvider
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeUpdater
import hardcoder.dev.logic.features.waterTracking.drinkType.ValidatedDrinkTypeName
import hardcoder.dev.logic.icons.IconResourceProvider
import hardcoder.dev.logic.icons.LocalIcon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DrinkTypeUpdateViewModel(
    private val drinkTypeId: Int,
    private val drinkTypeNameValidator: DrinkTypeNameValidator,
    private val drinkTypeProvider: DrinkTypeProvider,
    private val drinkTypeUpdater: DrinkTypeUpdater,
    private val drinkTypeDeleter: DrinkTypeDeleter,
    iconResourceProvider: IconResourceProvider,
) : ViewModel() {

    private val updateState = MutableStateFlow<UpdateState>(UpdateState.NotExecuted)
    private val deleteState = MutableStateFlow<DeleteState>(DeleteState.NotExecuted)
    private val availableIconsList = MutableStateFlow(iconResourceProvider.getIcons())
    private val selectedIcon = MutableStateFlow<LocalIcon>(iconResourceProvider.getIcon(0))
    private val selectedHydrationIndexPercentage = MutableStateFlow(0)
    private val name = MutableStateFlow<String?>(null)
    private val validatedDrinkTypeName = name.map {
        it?.let {
            drinkTypeNameValidator.validate(it)
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
        validatedDrinkTypeName,
        availableIconsList,
        selectedIcon,
        selectedHydrationIndexPercentage
    ) { updateState, deleteState, name, validatedName,
        availableIconsList, selectedIcon, selectedHydrationIndexPercentage ->
        State(
            updateState = updateState,
            deleteState = deleteState,
            name = name,
            validatedDrinkTypeName = validatedName,
            availableIconsList = availableIconsList,
            selectedIcon = selectedIcon,
            hydrationIndexPercentage = selectedHydrationIndexPercentage,
            allowUpdate = validatedName is CorrectValidatedDrinkTypeName
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            updateState = updateState.value,
            deleteState = deleteState.value,
            name = name.value,
            validatedDrinkTypeName = validatedDrinkTypeName.value,
            availableIconsList = availableIconsList.value,
            selectedIcon = selectedIcon.value,
            hydrationIndexPercentage = selectedHydrationIndexPercentage.value,
            allowUpdate = false
        )
    )

    fun updateName(newName: String) {
        name.value = newName
    }

    fun updateSelectedIcon(icon: LocalIcon) {
        selectedIcon.value = icon
    }

    fun updateHydrationIndexPercentage(hydrationIndexPercentage: Int) {
        selectedHydrationIndexPercentage.value = hydrationIndexPercentage
    }

    fun updateDrinkType() {
        viewModelScope.launch {
            val validatedName = validatedDrinkTypeName.value
            require(validatedName is CorrectValidatedDrinkTypeName)

            drinkTypeProvider.provideDrinkTypeById(drinkTypeId).firstOrNull()?.let {
                val updatedTrack = it.copy(
                    id = drinkTypeId,
                    name = validatedName.data,
                    icon = selectedIcon.value,
                    hydrationIndexPercentage = selectedHydrationIndexPercentage.value
                )
                drinkTypeUpdater.update(updatedTrack)
                updateState.value = UpdateState.Executed
            } ?: throw Resources.NotFoundException("Track not found by it's id")
        }
    }

    fun deleteById() {
        viewModelScope.launch {
            drinkTypeDeleter.deleteById(drinkTypeId)
            deleteState.value = DeleteState.Executed
        }
    }

    private fun fillStateWithUpdatedTrack() {
        viewModelScope.launch {
            drinkTypeProvider.provideDrinkTypeById(drinkTypeId).firstOrNull()?.let { drinkType ->
                name.value = drinkType.name
                selectedHydrationIndexPercentage.value = drinkType.hydrationIndexPercentage
                selectedIcon.value = drinkType.icon
            }
        }
    }

    data class State(
        val updateState: UpdateState,
        val deleteState: DeleteState,
        val allowUpdate: Boolean,
        val name: String?,
        val validatedDrinkTypeName: ValidatedDrinkTypeName?,
        val availableIconsList: List<LocalIcon>,
        val selectedIcon: LocalIcon,
        val hydrationIndexPercentage: Int
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