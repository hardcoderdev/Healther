package hardcoder.dev.logic.features.fasting.plan

import hardcoder.dev.logic.entities.features.fasting.FastingPlan
import kotlinx.coroutines.flow.flowOf

class FastingPlanProvider {

    fun provideAllPlans() = flowOf(FastingPlan.values().toList())
}