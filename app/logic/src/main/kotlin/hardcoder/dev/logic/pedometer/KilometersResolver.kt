package hardcoder.dev.logic.pedometer

class KilometersResolver {

    fun resolve(stepsCount: Int): Float {
        return (stepsCount / AVERAGE_STEPS_COUNT_IN_ONE_KILOMETER).toFloat()
    }

    private companion object {
        private const val AVERAGE_STEPS_COUNT_IN_ONE_KILOMETER = 1429
    }
}