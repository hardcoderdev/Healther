package hardcoder.dev.presentation.features.waterTracking.drinkType

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.logic.features.waterTracking.drinkType.CorrectValidatedDrinkTypeName
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeCreator
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeNameValidator
import hardcoder.dev.logic.features.waterTracking.drinkType.ValidatedDrinkTypeName
import hardcoder.dev.logic.icons.IconResourceProvider
import hardcoder.dev.logic.icons.LocalIcon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DrinkTypeCreateViewModel(
    private val drinkTypeCreator: DrinkTypeCreator,
    private val drinkTypeNameValidator: DrinkTypeNameValidator,
    iconResourceProvider: IconResourceProvider
) : ViewModel() {

    private val creationState = MutableStateFlow<CreationState>(CreationState.NotExecuted)
    private val availableIconsList = MutableStateFlow(iconResourceProvider.getIcons())
    private val selectedIcon = MutableStateFlow(iconResourceProvider.getIcon(0))
    private val selectedHydrationIndexPercentage =
        MutableStateFlow(DEFAULT_HYDRATION_INDEX_PERCENTAGE)
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

    val state = combine(
        creationState,
        name,
        validatedDrinkTypeName,
        availableIconsList,
        selectedIcon,
        selectedHydrationIndexPercentage
    ) { creationState, name, validatedName, availableIcons,
        selectedIcon, hydrationIndexPercentage ->
        State(
            creationState = creationState,
            name = name,
            validatedDrinkTypeName = validatedName,
            availableIconsList = availableIcons,
            selectedIcon = selectedIcon,
            hydrationIndexPercentage = hydrationIndexPercentage,
            allowCreation = validatedName is CorrectValidatedDrinkTypeName
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            allowCreation = false,
            creationState = creationState.value,
            name = name.value,
            validatedDrinkTypeName = validatedDrinkTypeName.value,
            availableIconsList = availableIconsList.value,
            selectedIcon = selectedIcon.value,
            hydrationIndexPercentage = selectedHydrationIndexPercentage.value
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

    fun createDrinkType() {
        viewModelScope.launch {
            val validatedName = validatedDrinkTypeName.value
            require(validatedName is CorrectValidatedDrinkTypeName)

            drinkTypeCreator.create(
                name = validatedName.data,
                iconId = selectedIcon.value.id,
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
        val availableIconsList: List<LocalIcon>,
        val selectedIcon: LocalIcon,
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