package hardcoder.dev.logic.features.moodTracking.hobby

class HobbyNameValidator {

    fun validate(hobbyName: HobbyName): ValidatedHobbyName {
        return hobbyName.incorrectReason()?.let { reason ->
            IncorrectHobbyName(data = hobbyName, reason = reason)
        } ?: run {
            CorrectHobbyName(data = hobbyName)
        }
    }

    private fun HobbyName.incorrectReason(): IncorrectHobbyName.Reason? {
        return when {
            value.isEmpty() -> IncorrectHobbyName.Reason.Empty
            value.length > HOBBY_NAME_MAX_CHARS -> IncorrectHobbyName.Reason.MoreThanMaxChars
            else -> null
        }
    }

    private companion object {
        private const val HOBBY_NAME_MAX_CHARS = 20
    }
}

sealed class ValidatedHobbyName {
    abstract val data: HobbyName
}

data class CorrectHobbyName(override val data: HobbyName) : ValidatedHobbyName()
data class IncorrectHobbyName(
    override val data: HobbyName,
    val reason: Reason
) : ValidatedHobbyName() {
    sealed class Reason {
        object Empty : Reason()
        object MoreThanMaxChars : Reason()
    }
}

data class HobbyName(val value: String)