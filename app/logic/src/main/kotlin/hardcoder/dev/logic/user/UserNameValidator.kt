package hardcoder.dev.logic.user

class UserNameValidator {

    fun validate(userName: String): ValidatedUserName {
        return userName.incorrectReason()?.let { reason ->
            IncorrectUserName(data = userName, reason = reason)
        } ?: run {
            CorrectUserName(data = userName)
        }
    }

    private fun String.incorrectReason(): IncorrectUserName.Reason? {
        return when {
            isBlank() -> IncorrectUserName.Reason.Empty
            length > USER_NAME_MAX_CHARS -> IncorrectUserName.Reason.MoreThanMaxChars(
                USER_NAME_MAX_CHARS,
            )

            else -> null
        }
    }

    private companion object {
        private const val USER_NAME_MAX_CHARS = 30
    }
}

sealed class ValidatedUserName {
    abstract val data: String
}

class CorrectUserName(
    override val data: String,
) : ValidatedUserName()

class IncorrectUserName(
    override val data: String,
    val reason: Reason,
) : ValidatedUserName() {
    sealed class Reason {
        data object Empty : Reason()
        data class MoreThanMaxChars(val maxChars: Int) : Reason()
    }
}
