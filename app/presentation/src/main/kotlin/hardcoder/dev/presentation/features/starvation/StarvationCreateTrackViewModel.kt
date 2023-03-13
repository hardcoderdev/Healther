package hardcoder.dev.presentation.features.starvation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.entities.features.starvation.StarvationPlan
import hardcoder.dev.logic.features.starvation.plan.StarvationPlanDurationResolver
import hardcoder.dev.logic.features.starvation.plan.StarvationPlanProvider
import hardcoder.dev.logic.features.starvation.track.StarvationCurrentIdManager
import hardcoder.dev.logic.features.starvation.track.StarvationTrackCreator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StarvationCreateTrackViewModel(
    private val idGenerator: IdGenerator,
    private val starvationCurrentIdManager: StarvationCurrentIdManager,
    private val starvationTrackCreator: StarvationTrackCreator,
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

    fun saveTrack() {
        val currentTrackId = idGenerator.nextId()
        val starvationPlanDurationInMillis = starvationPlanDurationMapper.resolve(
            selectedPlan.value
        )

        viewModelScope.launch {
            starvationCurrentIdManager.setCurrentId(currentTrackId)
            starvationTrackCreator.create(
                id = currentTrackId,
                startTime = System.currentTimeMillis(),
                duration = starvationPlanDurationInMillis,
                starvationPlan = selectedPlan.value
            )
        }
    }

    data class State(
        val selectedPlan: StarvationPlan,
        val starvationPlanList: List<StarvationPlan>
    )
}