package hardcoder.dev.logic.features.waterTracking

class WaterTrackMillilitersValidator {

    fun validate(
        millilitersCount: Int,
        dailyWaterIntakeInMillisCount: Int
    ): ValidatedMillilitersCount {
        return millilitersCount.incorrectReason(dailyWaterIntakeInMillisCount)?.let {
            IncorrectMillilitersCount(millilitersCount, it)
        } ?: run {
            CorrectMillilitersCount(millilitersCount)
        }
    }

    private fun Int.incorrectReason(dailyWaterIntakeInMillisCount: Int): IncorrectMillilitersCount.Reason? {
        return when {
            this > dailyWaterIntakeInMillisCount -> IncorrectMillilitersCount.Reason.MoreThanDailyWaterIntake()
            this == 0 -> IncorrectMillilitersCount.Reason.Empty()
            else -> null
        }
    }
}

sealed class ValidatedMillilitersCount {
    abstract val data: Int
}

data class CorrectMillilitersCount(
    override val data: Int
) : ValidatedMillilitersCount()

data class IncorrectMillilitersCount(
    override val data: Int,
    val reason: Reason
) : ValidatedMillilitersCount() {
    sealed class Reason {
        class Empty : Reason()
        class MoreThanDailyWaterIntake : Reason()
    }
}
