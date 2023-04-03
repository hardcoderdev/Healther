package hardcoder.dev.logic.features.moodTracking.hobby

class HobbyNameValidator {

    fun validate(hobbyName: String): ValidatedHobbyName {
        return hobbyName.incorrectReason()?.let { reason ->
            IncorrectHobbyName(data = hobbyName, reason = reason)
        } ?: run {
            CorrectHobbyName(data = hobbyName)
        }
    }

    private fun String.incorrectReason(): IncorrectHobbyName.Reason? {
        return when {
            isEmpty() -> IncorrectHobbyName.Reason.Empty
            length > HOBBY_NAME_MAX_CHARS -> IncorrectHobbyName.Reason.MoreThanMaxChars(
                HOBBY_NAME_MAX_CHARS
            )
            else -> null
        }
    }

    private companion object {
        private const val HOBBY_NAME_MAX_CHARS = 20
    }
}

sealed class ValidatedHobbyName {
    abstract val data: String
}

data class CorrectHobbyName(override val data: String) : ValidatedHobbyName()
data class IncorrectHobbyName(
    override val data: String,
    val reason: Reason
) : ValidatedHobbyName() {
    sealed class Reason {
        object Empty : Reason()
        data class MoreThanMaxChars(val value: Int) : Reason()
    }
}