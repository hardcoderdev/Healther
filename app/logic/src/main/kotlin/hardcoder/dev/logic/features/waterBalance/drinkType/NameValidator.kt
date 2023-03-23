package hardcoder.dev.logic.features.waterBalance.drinkType

class NameValidator {

    fun validate(data: Name) = data.incorrectReason()?.let { reason ->
        IncorrectValidatedName(data = data, reason = reason)
    } ?: run {
        CorrectValidatedName(data = data)
    }

    private fun Name.incorrectReason(): IncorrectValidatedName.Reason? {
        return when {
            value.isEmpty() -> IncorrectValidatedName.Reason.Empty
            value.length > DRINK_TYPE_NAME_MAX_CHARS -> IncorrectValidatedName.Reason.MoreThanMaxChars
            else -> null
        }
    }

    private companion object {
        private const val DRINK_TYPE_NAME_MAX_CHARS = 30
    }
}

sealed class ValidatedName {
    abstract val data: Name
}

data class CorrectValidatedName(
    override val data: Name
) : ValidatedName()

data class IncorrectValidatedName(
    override val data: Name,
    val reason: Reason
) : ValidatedName() {
    sealed class Reason {
        object Empty : Reason()
        object MoreThanMaxChars : Reason()
    }
}

data class Name(val value: String)