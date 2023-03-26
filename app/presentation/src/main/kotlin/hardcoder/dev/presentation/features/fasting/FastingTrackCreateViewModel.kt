package hardcoder.dev.presentation.features.fasting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.entities.features.fasting.FastingPlan
import hardcoder.dev.logic.features.fasting.plan.FastingPlanDurationResolver
import hardcoder.dev.logic.features.fasting.plan.FastingPlanProvider
import hardcoder.dev.logic.features.fasting.track.CurrentFastingManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FastingTrackCreateViewModel(
    private val currentFastingManager: CurrentFastingManager,
    private val fastingPlanDurationMapper: FastingPlanDurationResolver,
    fastingPlanProvider: FastingPlanProvider
) : ViewModel() {

    private val creationState = MutableStateFlow<CreationState>(CreationState.NotExecuted)
    private val customFastingHours = MutableStateFlow<Int?>(null)
    private val selectedPlan = MutableStateFlow<FastingPlan?>(null)
    private val fastingPlanList = fastingPlanProvider.provideAllPlans().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            selectedPlan.value = fastingPlanList.value.first()
        }
    }

    val state = combine(
        creationState,
        fastingPlanList,
        selectedPlan
    ) { creationState, fastingPlanList, selectedPlan ->
        State(
            creationState = creationState,
            creationAllowed = selectedPlan != null,
            selectedPlan = selectedPlan,
            fastingPlanList = fastingPlanList
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            creationState = creationState.value,
            creationAllowed = false,
            selectedPlan = selectedPlan.value,
            fastingPlanList = fastingPlanList.value
        )
    )

    fun updateFastingPlan(plan: FastingPlan, newCustomStarvingHours: Int?) {
        selectedPlan.value = plan
        customFastingHours.value = newCustomStarvingHours
    }

    fun startFasting() {
        viewModelScope.launch {
            val fastingPlan = requireNotNull(selectedPlan.value)

            currentFastingManager.startFasting(
                startTime = System.currentTimeMillis(),
                fastingPlan = fastingPlan,
                duration = fastingPlanDurationMapper.resolve(
                    fastingPlan,
                    customFastingHours.value
                )
            )
        }
        creationState.value = CreationState.Executed
    }

    data class State(
        val creationState: CreationState,
        val creationAllowed: Boolean,
        val selectedPlan: FastingPlan?,
        val fastingPlanList: List<FastingPlan>
    )

    sealed class CreationState {
        object NotExecuted : CreationState()
        object Executed : CreationState()
    }
}