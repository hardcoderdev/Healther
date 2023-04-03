package hardcoder.dev.logic.features.waterTracking

class WaterTrackMillilitersValidator {

    fun validate(
        millilitersCount: Int,
        dailyWaterIntakeInMillisCount: Int
    ): ValidatedMillilitersCount {
        return millilitersCount.incorrectReason(dailyWaterIntakeInMillisCount)?.let {
            IncorrectMillilitersInput(millilitersCount, it)
        } ?: run {
            CorrectMillilitersInput(millilitersCount)
        }
    }

    private fun Int.incorrectReason(dailyWaterIntakeInMillisCount: Int): IncorrectMillilitersInput.Reason? {
        return when {
            this > dailyWaterIntakeInMillisCount -> IncorrectMillilitersInput.Reason.MoreThanDailyWaterIntake()
            this == 0 -> IncorrectMillilitersInput.Reason.Empty()
            else -> null
        }
    }
}

sealed class ValidatedMillilitersCount {
    abstract val data: Int
}

data class CorrectMillilitersInput(
    override val data: Int
) : ValidatedMillilitersCount()

data class IncorrectMillilitersInput(
    override val data: Int,
    val reason: Reason
) : ValidatedMillilitersCount() {
    sealed class Reason {
        class Empty : Reason()
        class MoreThanDailyWaterIntake : Reason()
    }
}
