package hardcoder.dev.logic.pedometer

class CaloriesResolver {

    fun resolve(weight: Int): Float {
        return weight * CALORIES_IN_HOUR_FOR_ONE_KILOGRAM
    }

    companion object {
        private const val CALORIES_IN_HOUR_FOR_ONE_KILOGRAM = 3.2f
    }
}