package hardcoder.dev.logic.features.waterBalance.drinkType

class DrinkTypeNameValidator {

    fun validate(data: DrinkTypeName) = data.incorrectReason()?.let { reason ->
        IncorrectValidatedDrinkTypeName(data = data, reason = reason)
    } ?: run {
        CorrectValidatedDrinkTypeName(data = data)
    }

    private fun DrinkTypeName.incorrectReason(): IncorrectValidatedDrinkTypeName.Reason? {
        return when {
            value.isEmpty() -> IncorrectValidatedDrinkTypeName.Reason.Empty
            value.length > DRINK_TYPE_NAME_MAX_CHARS -> IncorrectValidatedDrinkTypeName.Reason.MoreThanMaxChars
            else -> null
        }
    }

    private companion object {
        private const val DRINK_TYPE_NAME_MAX_CHARS = 30
    }
}

sealed class ValidatedDrinkTypeName {
    abstract val data: DrinkTypeName
}

data class CorrectValidatedDrinkTypeName(
    override val data: DrinkTypeName
) : ValidatedDrinkTypeName()

data class IncorrectValidatedDrinkTypeName(
    override val data: DrinkTypeName,
    val reason: Reason
) : ValidatedDrinkTypeName() {
    sealed class Reason {
        object Empty : Reason()
        object MoreThanMaxChars : Reason()
    }
}

data class DrinkTypeName(val value: String)