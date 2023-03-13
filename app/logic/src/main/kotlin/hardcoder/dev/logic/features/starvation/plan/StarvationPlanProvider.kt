package hardcoder.dev.logic.features.starvation.plan

import hardcoder.dev.entities.features.starvation.StarvationPlan
import kotlinx.coroutines.flow.flowOf

class StarvationPlanProvider {

    fun provideAllPlans() = flowOf(StarvationPlan.values().toList())
}