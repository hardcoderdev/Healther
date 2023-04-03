package hardcoder.dev.logic.features.waterTracking.drinkType

class DrinkTypeNameValidator {

    fun validate(drinkTypeName: String): ValidatedDrinkTypeName {
        return drinkTypeName.incorrectReason()?.let { reason ->
            IncorrectValidatedDrinkTypeName(data = drinkTypeName, reason = reason)
        } ?: run {
            CorrectValidatedDrinkTypeName(data = drinkTypeName)
        }
    }

    private fun String.incorrectReason(): IncorrectValidatedDrinkTypeName.Reason? {
        return when {
            isEmpty() -> IncorrectValidatedDrinkTypeName.Reason.Empty
            length > DRINK_TYPE_NAME_MAX_CHARS -> IncorrectValidatedDrinkTypeName.Reason.MoreThanMaxChars
            else -> null
        }
    }

    private companion object {
        private const val DRINK_TYPE_NAME_MAX_CHARS = 30
    }
}

sealed class ValidatedDrinkTypeName {
    abstract val data: String
}

data class CorrectValidatedDrinkTypeName(
    override val data: String
) : ValidatedDrinkTypeName()

data class IncorrectValidatedDrinkTypeName(
    override val data: String,
    val reason: Reason
) : ValidatedDrinkTypeName() {
    sealed class Reason {
        object Empty : Reason()
        object MoreThanMaxChars : Reason()
    }
}