package hardcoder.dev.presentation.features.starvation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.entities.features.starvation.StarvationPlan
import hardcoder.dev.logic.features.starvation.plan.StarvationPlanDurationResolver
import hardcoder.dev.logic.features.starvation.plan.StarvationPlanProvider
import hardcoder.dev.logic.features.starvation.track.StarvationCurrentTrackManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StarvationCreateTrackViewModel(
    private val starvationCurrentTrackManager: StarvationCurrentTrackManager,
    private val starvationPlanDurationMapper: StarvationPlanDurationResolver,
    starvationPlanProvider: StarvationPlanProvider
) : ViewModel() {

    private val selectedPlan = MutableStateFlow(StarvationPlan.PLAN_14_10)
    private val starvationPlanList = starvationPlanProvider.provideAllPlans().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    val state = combine(
        starvationPlanList,
        selectedPlan
    ) { starvationPlanList, selectedPlan ->
        State(
            selectedPlan = selectedPlan,
            starvationPlanList = starvationPlanList
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            selectedPlan = selectedPlan.value,
            starvationPlanList = starvationPlanList.value
        )
    )

    fun updateStarvationPlan(plan: StarvationPlan) {
        selectedPlan.value = plan
    }

    fun startStarvation() {
        viewModelScope.launch {
            starvationCurrentTrackManager.startStarvation(
                startTime = System.currentTimeMillis(),
                duration = starvationPlanDurationMapper.resolve(selectedPlan.value),
                starvationPlan = selectedPlan.value
            )
        }
    }

    data class State(
        val selectedPlan: StarvationPlan,
        val starvationPlanList: List<StarvationPlan>
    )
}