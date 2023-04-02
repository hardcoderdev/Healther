package hardcoder.dev.presentation.features.waterBalance.drinkType

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.logic.features.general.CorrectValidatedIconResource
import hardcoder.dev.logic.features.general.IconResource
import hardcoder.dev.logic.features.general.IconResourceValidator
import hardcoder.dev.logic.features.general.ValidatedIconResource
import hardcoder.dev.logic.features.waterBalance.drinkType.CorrectValidatedDrinkTypeName
import hardcoder.dev.logic.features.waterBalance.drinkType.DrinkTypeCreator
import hardcoder.dev.logic.features.waterBalance.drinkType.DrinkTypeName
import hardcoder.dev.logic.features.waterBalance.drinkType.DrinkTypeNameValidator
import hardcoder.dev.logic.features.waterBalance.drinkType.ValidatedDrinkTypeName
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
    private val iconResourceValidator: IconResourceValidator,
    iconResourceProvider: IconResourceProvider
) : ViewModel() {

    private val creationState = MutableStateFlow<CreationState>(CreationState.NotExecuted)
    private val availableIconResourceList =
        MutableStateFlow(iconResourceProvider.provideWaterTrackingIconResources())
    private val selectedIconResource = MutableStateFlow<String?>(null)
    private val selectedHydrationIndexPercentage =
        MutableStateFlow(DEFAULT_HYDRATION_INDEX_PERCENTAGE)
    private val name = MutableStateFlow<String?>(null)
    private val validatedDrinkTypeName = name.map {
        it?.let {
            drinkTypeNameValidator.validate(DrinkTypeName(it))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )
    private val validatedIconResource = selectedIconResource.map {
        it?.let {
            iconResourceValidator.validate(IconResource(it))
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
        validatedDrinkTypeName,
        availableIconResourceList,
        selectedIconResource,
        validatedIconResource,
        selectedHydrationIndexPercentage
    ) { creationState, name, validatedName, availableIcons,
        selectedIconResource, validatedIconResource, hydrationIndexPercentage ->
        State(
            creationState = creationState,
            name = name,
            validatedDrinkTypeName = validatedName,
            availableIconResourceList = availableIcons,
            selectedIconResource = selectedIconResource,
            validatedIconResource = validatedIconResource,
            hydrationIndexPercentage = hydrationIndexPercentage,
            allowCreation = validatedName is CorrectValidatedDrinkTypeName
                    && validatedIconResource is CorrectValidatedIconResource,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            allowCreation = false,
            creationState = creationState.value,
            name = name.value,
            validatedDrinkTypeName = validatedDrinkTypeName.value,
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
            val validatedName = validatedDrinkTypeName.value
            require(validatedName is CorrectValidatedDrinkTypeName)

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
        val validatedDrinkTypeName: ValidatedDrinkTypeName?,
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