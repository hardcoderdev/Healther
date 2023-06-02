package hardcoder.dev.logic.features.moodTracking.activity

class ActivityNameValidator {

    fun validate(activityName: String): ValidatedActivityName {
        return activityName.incorrectReason()?.let { reason ->
            IncorrectActivityName(data = activityName, reason = reason)
        } ?: run {
            CorrectActivityName(data = activityName)
        }
    }

    private fun String.incorrectReason(): IncorrectActivityName.Reason? {
        return when {
            isEmpty() -> IncorrectActivityName.Reason.Empty
            length > ACTIVITY_NAME_MAX_CHARS -> IncorrectActivityName.Reason.MoreThanMaxChars(
                ACTIVITY_NAME_MAX_CHARS
            )
            else -> null
        }
    }

    private companion object {
        private const val ACTIVITY_NAME_MAX_CHARS = 20
    }
}

sealed class ValidatedActivityName {
    abstract val data: String
}

data class CorrectActivityName(
    override val data: String
) : ValidatedActivityName()

data class IncorrectActivityName(
    override val data: String,
    val reason: Reason
) : ValidatedActivityName() {
    sealed class Reason {
        object Empty : Reason()
        data class MoreThanMaxChars(val maxChars: Int) : Reason()
    }
}