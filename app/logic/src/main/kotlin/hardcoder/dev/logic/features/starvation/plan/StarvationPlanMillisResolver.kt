package hardcoder.dev.logic.features.starvation.plan

class StarvationPlanMillisResolver {

    fun resolve(starvationPlanDurationInMillis: Long) =
        starvationPlanDurationInMillis / 1000 / 60 / 60
}