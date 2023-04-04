package hardcoder.dev.logic.features.fasting.plan

import kotlinx.coroutines.flow.flowOf

class FastingPlanProvider {

    fun provideAllPlans() = flowOf(FastingPlan.values().toList())
}