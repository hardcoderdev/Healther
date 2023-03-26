package hardcoder.dev.presentation.features.waterBalance.drinkType

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.logic.features.waterBalance.drinkType.CorrectValidatedIconResource
import hardcoder.dev.logic.features.waterBalance.drinkType.CorrectValidatedName
import hardcoder.dev.logic.features.waterBalance.drinkType.DrinkTypeDeleter
import hardcoder.dev.logic.features.waterBalance.drinkType.DrinkTypeIconResourceValidator
import hardcoder.dev.logic.features.waterBalance.drinkType.DrinkTypeNameValidator
import hardcoder.dev.logic.features.waterBalance.drinkType.DrinkTypeProvider
import hardcoder.dev.logic.features.waterBalance.drinkType.DrinkTypeUpdater
import hardcoder.dev.logic.features.waterBalance.drinkType.IconResource
import hardcoder.dev.logic.features.waterBalance.drinkType.Name
import hardcoder.dev.logic.features.waterBalance.drinkType.ValidatedIconResource
import hardcoder.dev.logic.features.waterBalance.drinkType.ValidatedName
import hardcoder.dev.logic.icons.IconResourceProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DrinkTypeUpdateViewModel(
    private val drinkTypeId: Int,
    private val drinkTypeNameValidator: DrinkTypeNameValidator,
    private val drinkTypeIconResourceValidator: DrinkTypeIconResourceValidator,
    private val drinkTypeProvider: DrinkTypeProvider,
    private val drinkTypeUpdater: DrinkTypeUpdater,
    private val drinkTypeDeleter: DrinkTypeDeleter,
    iconResourceProvider: IconResourceProvider,
) : ViewModel() {

    private val updateState = MutableStateFlow<UpdateState>(UpdateState.NotExecuted)
    private val deleteState = MutableStateFlow<DeleteState>(DeleteState.NotExecuted)
    private val availableIconResourceList =
        MutableStateFlow(iconResourceProvider.provideWaterTrackingIconResources())
    private val selectedIconResource = MutableStateFlow<String?>(null)
    private val selectedHydrationIndexPercentage = MutableStateFlow(0)
    private val name = MutableStateFlow<String?>(null)
    private val validatedName = name.map {
        it?.let {
            drinkTypeNameValidator.validate(Name(it))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )
    private val validatedIconResource = selectedIconResource.map {
        it?.let {
            drinkTypeIconResourceValidator.validate(IconResource(it))
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
        validatedName,
        availableIconResourceList,
        selectedIconResource,
        validatedIconResource,
        selectedHydrationIndexPercentage
    ) { updateState, deleteState, name, validatedName, availableIconResourceList, selectedIconResource,
        validatedIconResource, selectedHydrationIndexPercentage ->
        State(
            updateState = updateState,
            deleteState = deleteState,
            name = name,
            validatedName = validatedName,
            availableIconResourceList = availableIconResourceList,
            selectedIconResource = selectedIconResource,
            validatedIconResource = validatedIconResource,
            hydrationIndexPercentage = selectedHydrationIndexPercentage,
            allowUpdate = validatedName is CorrectValidatedName
                    && validatedIconResource is CorrectValidatedIconResource,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            updateState = updateState.value,
            deleteState = deleteState.value,
            name = name.value,
            validatedName = validatedName.value,
            availableIconResourceList = availableIconResourceList.value,
            selectedIconResource = selectedIconResource.value,
            validatedIconResource = validatedIconResource.value,
            hydrationIndexPercentage = selectedHydrationIndexPercentage.value,
            allowUpdate = false
        )
    )

    fun updateName(newName: String) {
        name.value = newName
    }

    fun updateSelectedIconResource(iconResourceName: String) {
        selectedIconResource.value = iconResourceName
    }

    fun updateHydrationIndexPercentage(hydrationIndexPercentage: Int) {
        selectedHydrationIndexPercentage.value = hydrationIndexPercentage
    }

    fun updateDrinkType() {
        viewModelScope.launch {
            val validatedName = validatedName.value
            require(validatedName is CorrectValidatedName)

            val validatedIconResource = validatedIconResource.value
            require(validatedIconResource is CorrectValidatedIconResource)

            drinkTypeProvider.provideDrinkTypeById(drinkTypeId).firstOrNull()?.let {
                val updatedTrack = it.copy(
                    id = drinkTypeId,
                    name = validatedName.data.value,
                    iconResourceName = validatedIconResource.data.value,
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
            selectedIconResource.value = availableIconResourceList.first().first()

            drinkTypeProvider.provideDrinkTypeById(drinkTypeId).firstOrNull()?.let { drinkType ->
                name.value = drinkType.name
                selectedHydrationIndexPercentage.value = drinkType.hydrationIndexPercentage
                selectedIconResource.value = drinkType.iconResourceName
            }
        }
    }

    data class State(
        val updateState: UpdateState,
        val deleteState: DeleteState,
        val allowUpdate: Boolean,
        val name: String?,
        val validatedName: ValidatedName?,
        val availableIconResourceList: List<String>,
        val selectedIconResource: String?,
        val validatedIconResource: ValidatedIconResource?,
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