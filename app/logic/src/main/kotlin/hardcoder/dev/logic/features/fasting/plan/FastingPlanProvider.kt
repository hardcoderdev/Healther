package hardcoder.dev.logic.features.fasting.plan

import hardcoder.dev.entities.features.fasting.FastingPlan
import kotlinx.coroutines.flow.flowOf

class FastingPlanProvider {

    fun provideAllPlans() = flowOf(FastingPlan.values().toList())
}