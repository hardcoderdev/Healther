package hardcoder.dev.logic.features.moodTracking.moodType

class MoodTypeNameValidator {

    fun validate(data: MoodTypeName) = data.incorrectReason()?.let { reason ->
        IncorrectValidatedMoodTypeName(data = data, reason = reason)
    } ?: run {
        CorrectValidatedMoodTypeName(data = data)
    }

    private fun MoodTypeName.incorrectReason(): IncorrectValidatedMoodTypeName.Reason? {
        return when {
            value.isEmpty() -> IncorrectValidatedMoodTypeName.Reason.Empty
            value.length > MOOD_TYPE_NAME_MAX_CHARS -> IncorrectValidatedMoodTypeName.Reason.MoreThanMaxChars
            else -> null
        }
    }

    private companion object {
        private const val MOOD_TYPE_NAME_MAX_CHARS = 30
    }
}

sealed class ValidatedMoodTypeName {
    abstract val data: MoodTypeName
}

data class CorrectValidatedMoodTypeName(
    override val data: MoodTypeName
) : ValidatedMoodTypeName()

data class IncorrectValidatedMoodTypeName(
    override val data: MoodTypeName,
    val reason: Reason
) : ValidatedMoodTypeName() {
    sealed class Reason {
        object Empty : Reason()
        object MoreThanMaxChars : Reason()
    }
}

data class MoodTypeName(val value: String)