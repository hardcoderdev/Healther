package hardcoder.dev.presentation.features.waterBalance.drinkType

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.logic.features.waterBalance.drinkType.CorrectValidatedIconResource
import hardcoder.dev.logic.features.waterBalance.drinkType.CorrectValidatedName
import hardcoder.dev.logic.features.waterBalance.drinkType.DrinkTypeCreator
import hardcoder.dev.logic.features.waterBalance.drinkType.DrinkTypeIconResourceValidator
import hardcoder.dev.logic.features.waterBalance.drinkType.DrinkTypeNameValidator
import hardcoder.dev.logic.features.waterBalance.drinkType.IconResource
import hardcoder.dev.logic.features.waterBalance.drinkType.Name
import hardcoder.dev.logic.features.waterBalance.drinkType.ValidatedIconResource
import hardcoder.dev.logic.features.waterBalance.drinkType.ValidatedName
import hardcoder.dev.logic.icons.IconResourceProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DrinkTypeCreateViewModel(
    private val drinkTypeCreator: DrinkTypeCreator,
    private val drinkTypeNameValidator: DrinkTypeNameValidator,
    private val drinkTypeIconResourceValidator: DrinkTypeIconResourceValidator,
    iconResourceProvider: IconResourceProvider
) : ViewModel() {

    private val creationState = MutableStateFlow<CreationState>(CreationState.NotExecuted)
    private val availableIconResourceList =
        MutableStateFlow(iconResourceProvider.provideWaterTrackingIconResources())
    private val selectedIconResource = MutableStateFlow<String?>(null)
    private val selectedHydrationIndexPercentage =
        MutableStateFlow(DEFAULT_HYDRATION_INDEX_PERCENTAGE)
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
        viewModelScope.launch {
            selectedIconResource.value = availableIconResourceList.first().first()
        }
    }

    val state = combine(
        creationState,
        name,
        validatedName,
        availableIconResourceList,
        selectedIconResource,
        validatedIconResource,
        selectedHydrationIndexPercentage
    ) { creationState, name, validatedName, availableIcons,
        selectedIconResource, validatedIconResource, hydrationIndexPercentage ->
        State(
            creationState = creationState,
            name = name,
            validatedName = validatedName,
            availableIconResourceList = availableIcons,
            selectedIconResource = selectedIconResource,
            validatedIconResource = validatedIconResource,
            hydrationIndexPercentage = hydrationIndexPercentage,
            allowCreation = validatedName is CorrectValidatedName
                    && validatedIconResource is CorrectValidatedIconResource,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            allowCreation = false,
            creationState = creationState.value,
            name = name.value,
            validatedName = validatedName.value,
            availableIconResourceList = availableIconResourceList.value,
            selectedIconResource = selectedIconResource.value,
            validatedIconResource = validatedIconResource.value,
            hydrationIndexPercentage = selectedHydrationIndexPercentage.value
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

    fun createDrinkType() {
        viewModelScope.launch {
            val validatedName = validatedName.value
            require(validatedName is CorrectValidatedName)

            val validatedIconResource = validatedIconResource.value
            require(validatedIconResource is CorrectValidatedIconResource)

            drinkTypeCreator.create(
                name = validatedName.data.value,
                iconResourceName = validatedIconResource.data.value,
                hydrationIndexPercentage = selectedHydrationIndexPercentage.value
            )
            creationState.value = CreationState.Executed
        }
    }

    data class State(
        val allowCreation: Boolean,
        val creationState: CreationState,
        val name: String?,
        val validatedName: ValidatedName?,
        val availableIconResourceList: List<String>,
        val selectedIconResource: String?,
        val validatedIconResource: ValidatedIconResource?,
        val hydrationIndexPercentage: Int
    )

    sealed class CreationState {
        object Executed : CreationState()
        object NotExecuted : CreationState()
    }

    private companion object {
        const val DEFAULT_HYDRATION_INDEX_PERCENTAGE = 30
    }
}