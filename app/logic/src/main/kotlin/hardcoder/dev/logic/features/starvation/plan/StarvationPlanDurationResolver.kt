package hardcoder.dev.logic.features.starvation.plan

import hardcoder.dev.datetime.TimeUnitMapper
import hardcoder.dev.entities.features.starvation.StarvationPlan

class StarvationPlanDurationResolver(timeUnitMapper: TimeUnitMapper) {

    private val map = mapOf(
        StarvationPlan.PLAN_14_10 to timeUnitMapper.hoursToMillis(14),
        StarvationPlan.PLAN_16_8 to timeUnitMapper.hoursToMillis(16),
        StarvationPlan.PLAN_18_6 to timeUnitMapper.hoursToMillis(18),
        StarvationPlan.PLAN_20_4 to timeUnitMapper.hoursToMillis(20),
        StarvationPlan.CUSTOM_PLAN to timeUnitMapper.hoursToMillis(1440)
    )

    fun resolve(starvationPlan: StarvationPlan) = checkNotNull(map[starvationPlan])
}
