package hardcoder.dev.logic.features.waterBalance

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
            value == 0 -> IncorrectMillilitersInput.Reason.Empty()
            else -> null
        }
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
        class Empty : Reason()
        class MoreThanDailyWaterIntake : Reason()
    }
}

data class MillilitersCount(val value: Int)

