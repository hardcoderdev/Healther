package hardcoder.dev.logic.features.moodTracking.moodType

class MoodTypeNameValidator {

    fun validate(moodTypeName: String) = moodTypeName.incorrectReason()?.let { reason ->
        IncorrectMoodTypeName(data = moodTypeName, reason = reason)
    } ?: run {
        CorrectMoodTypeName(data = moodTypeName)
    }

    private fun String.incorrectReason(): IncorrectMoodTypeName.Reason? {
        return when {
            isBlank() -> IncorrectMoodTypeName.Reason.Empty
            length > MOOD_TYPE_NAME_MAX_CHARS -> IncorrectMoodTypeName.Reason.MoreThanMaxChars(
                MOOD_TYPE_NAME_MAX_CHARS,
            )
            else -> null
        }
    }

    private companion object {
        private const val MOOD_TYPE_NAME_MAX_CHARS = 30
    }
}

sealed class ValidatedMoodTypeName {
    abstract val data: String
}

data class CorrectMoodTypeName(
    override val data: String,
) : ValidatedMoodTypeName()

data class IncorrectMoodTypeName(
    override val data: String,
    val reason: Reason,
) : ValidatedMoodTypeName() {
    sealed class Reason {
        object Empty : Reason()
        data class MoreThanMaxChars(val maxChars: Int) : Reason()
    }
}