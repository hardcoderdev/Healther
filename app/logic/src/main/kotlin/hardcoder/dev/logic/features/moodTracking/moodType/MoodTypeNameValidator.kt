package hardcoder.dev.logic.features.moodTracking.moodType

class MoodTypeNameValidator {

    fun validate(moodTypeName: String) = moodTypeName.incorrectReason()?.let { reason ->
        IncorrectValidatedMoodTypeName(data = moodTypeName, reason = reason)
    } ?: run {
        CorrectValidatedMoodTypeName(data = moodTypeName)
    }

    private fun String.incorrectReason(): IncorrectValidatedMoodTypeName.Reason? {
        return when {
            isEmpty() -> IncorrectValidatedMoodTypeName.Reason.Empty
            length > MOOD_TYPE_NAME_MAX_CHARS -> IncorrectValidatedMoodTypeName.Reason.MoreThanMaxChars(
                MOOD_TYPE_NAME_MAX_CHARS
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

data class CorrectValidatedMoodTypeName(
    override val data: String
) : ValidatedMoodTypeName()

data class IncorrectValidatedMoodTypeName(
    override val data: String,
    val reason: Reason
) : ValidatedMoodTypeName() {
    sealed class Reason {
        object Empty : Reason()
        data class MoreThanMaxChars(val value: Int) : Reason()
    }
}