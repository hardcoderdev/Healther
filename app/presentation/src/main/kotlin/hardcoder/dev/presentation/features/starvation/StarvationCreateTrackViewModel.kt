package hardcoder.dev.presentation.features.starvation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.entities.features.starvation.StarvationPlan
import hardcoder.dev.logic.features.starvation.plan.StarvationPlanDurationResolver
import hardcoder.dev.logic.features.starvation.plan.StarvationPlanProvider
import hardcoder.dev.logic.features.starvation.track.CurrentStarvationManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StarvationCreateTrackViewModel(
    private val currentStarvationManager: CurrentStarvationManager,
    private val starvationPlanDurationMapper: StarvationPlanDurationResolver,
    starvationPlanProvider: StarvationPlanProvider
) : ViewModel() {

    private val creationState = MutableStateFlow<CreationState>(CreationState.NotExecuted)
    private val customStarvingHours = MutableStateFlow<Int?>(null)
    private val selectedPlan = MutableStateFlow<StarvationPlan?>(null)
    private val starvationPlanList = starvationPlanProvider.provideAllPlans().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            selectedPlan.value = starvationPlanList.value.first()
        }
    }

    val state = combine(
        creationState,
        starvationPlanList,
        selectedPlan
    ) { creationState, starvationPlanList, selectedPlan ->
        State(
            creationState = creationState,
            creationAllowed = selectedPlan != null,
            selectedPlan = selectedPlan,
            starvationPlanList = starvationPlanList
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            creationState = creationState.value,
            creationAllowed = false,
            selectedPlan = selectedPlan.value,
            starvationPlanList = starvationPlanList.value
        )
    )

    fun updateStarvationPlan(plan: StarvationPlan, newCustomStarvingHours: Int?) {
        selectedPlan.value = plan
        customStarvingHours.value = newCustomStarvingHours
    }

    fun startStarvation() {
        viewModelScope.launch {
            val starvationPlan = requireNotNull(selectedPlan.value)

            currentStarvationManager.startStarvation(
                startTime = System.currentTimeMillis(),
                starvationPlan = starvationPlan,
                duration = starvationPlanDurationMapper.resolve(
                    starvationPlan,
                    customStarvingHours.value
                )
            )
        }
        creationState.value = CreationState.Executed
    }

    data class State(
        val creationState: CreationState,
        val creationAllowed: Boolean,
        val selectedPlan: StarvationPlan?,
        val starvationPlanList: List<StarvationPlan>
    )

    sealed class CreationState {
        object NotExecuted : CreationState()
        object Executed : CreationState()
    }
}