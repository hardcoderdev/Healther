package hardcoder.dev.presentation.features.moodTracking.moodType

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.logic.features.general.CorrectValidatedIconResource
import hardcoder.dev.logic.features.general.IconResource
import hardcoder.dev.logic.features.general.IconResourceValidator
import hardcoder.dev.logic.features.general.ValidatedIconResource
import hardcoder.dev.logic.features.moodTracking.moodType.CorrectValidatedMoodTypeName
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeCreator
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeName
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeNameValidator
import hardcoder.dev.logic.features.moodTracking.moodType.ValidatedMoodTypeName
import hardcoder.dev.logic.icons.IconResourceProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MoodTypeCreateViewModel(
    private val moodTypeCreator: MoodTypeCreator,
    private val moodTypeNameValidator: MoodTypeNameValidator,
    private val iconResourceValidator: IconResourceValidator,
    iconResourceProvider: IconResourceProvider
) : ViewModel() {

    private val creationState = MutableStateFlow<CreationState>(CreationState.NotExecuted)
    private val availableIconResourceList =
        MutableStateFlow(iconResourceProvider.provideMoodTrackingMoodTypesIconsResources())
    private val selectedIconResource = MutableStateFlow<String?>(null)
    private val selectedPositivePercentage = MutableStateFlow(DEFAULT_POSITIVE_PERCENTAGE)
    private val name = MutableStateFlow<String?>(null)
    private val validatedMoodTypeName = name.map {
        it?.let {
            moodTypeNameValidator.validate(MoodTypeName(it))
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
        validatedMoodTypeName,
        availableIconResourceList,
        selectedIconResource,
        validatedIconResource,
        selectedPositivePercentage
    ) { creationState, name, validatedName, availableIcons,
        selectedIconResource, validatedIconResource, selectedPositivePercentage ->
        State(
            creationState = creationState,
            name = name,
            validatedMoodTypeName = validatedName,
            availableIconResourceList = availableIcons,
            selectedIconResource = selectedIconResource,
            validatedIconResource = validatedIconResource,
            positivePercentage = selectedPositivePercentage,
            allowCreation = validatedName is CorrectValidatedMoodTypeName
                    && validatedIconResource is CorrectValidatedIconResource,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            allowCreation = false,
            creationState = creationState.value,
            name = name.value,
            validatedMoodTypeName = validatedMoodTypeName.value,
            availableIconResourceList = availableIconResourceList.value,
            selectedIconResource = selectedIconResource.value,
            validatedIconResource = validatedIconResource.value,
            positivePercentage = selectedPositivePercentage.value
        )
    )

    fun updateName(newName: String) {
        name.value = newName
    }

    fun updateSelectedIconResource(iconResourceName: String) {
        selectedIconResource.value = iconResourceName
    }

    fun updatePositivePercentage(positivePercentage: Int) {
        selectedPositivePercentage.value = positivePercentage
    }

    fun createMoodType() {
        viewModelScope.launch {
            val validatedName = validatedMoodTypeName.value
            require(validatedName is CorrectValidatedMoodTypeName)

            val validatedIconResource = validatedIconResource.value
            require(validatedIconResource is CorrectValidatedIconResource)

            moodTypeCreator.create(
                name = validatedName.data.value,
                iconResourceName = validatedIconResource.data.value,
                positivePercentage = selectedPositivePercentage.value
            )

            creationState.value = CreationState.Executed
        }
    }

    data class State(
        val allowCreation: Boolean,
        val creationState: CreationState,
        val name: String?,
        val validatedMoodTypeName: ValidatedMoodTypeName?,
        val availableIconResourceList: List<String>,
        val selectedIconResource: String?,
        val validatedIconResource: ValidatedIconResource?,
        val positivePercentage: Int
    )

    sealed class CreationState {
        object Executed : CreationState()
        object NotExecuted : CreationState()
    }

    private companion object {
        const val DEFAULT_POSITIVE_PERCENTAGE = 10
    }
}