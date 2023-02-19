package hardcoder.dev.healther.logic.validators

class WaterTrackMillilitersValidator {

    fun validate(
        data: MillilitersCount,
        dailyWaterIntakeInMillisCount: Int
    ) = data.incorrectReason(dailyWaterIntakeInMillisCount)?.let {
        IncorrectMillilitersInput(data, it)
    } ?: CorrectMillilitersInput(data)

    private fun MillilitersCount.incorrectReason(dailyWaterIntakeInMillisCount: Int): IncorrectMillilitersInput.Reason? {
        return when {
            value > dailyWaterIntakeInMillisCount -> IncorrectMillilitersInput.Reason.MoreThanDailyWaterIntake()
            value < MINIMUM_MILLILITERS -> IncorrectMillilitersInput.Reason.LessThanMinimum()
            else -> null
        }
    }

    private companion object {
        private const val MINIMUM_MILLILITERS = 100
    }
}

sealed class ValidatedMillilitersCount {
    abstract val data: MillilitersCount
}

data class CorrectMillilitersInput(
    override val data: MillilitersCount
) : ValidatedMillilitersCount()

data class IncorrectMillilitersInput(
    override val data: MillilitersCount,
    val reason: Reason
) : ValidatedMillilitersCount() {
    sealed class Reason {
        class LessThanMinimum : Reason()
        class MoreThanDailyWaterIntake : Reason()
    }
}

data class MillilitersCount(val value: Int)

