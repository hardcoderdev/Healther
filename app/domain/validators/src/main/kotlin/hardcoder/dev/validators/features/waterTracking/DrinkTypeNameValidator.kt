package hardcoder.dev.validators.features.waterTracking

class DrinkTypeNameValidator {

    fun validate(drinkTypeName: String): ValidatedDrinkTypeName {
        return drinkTypeName.incorrectReason()?.let { reason ->
            IncorrectDrinkTypeName(data = drinkTypeName, reason = reason)
        } ?: run {
            CorrectDrinkTypeName(data = drinkTypeName)
        }
    }

    private fun String.incorrectReason(): IncorrectDrinkTypeName.Reason? {
        return when {
            isBlank() -> IncorrectDrinkTypeName.Reason.Empty
            length > DRINK_TYPE_NAME_MAX_CHARS -> IncorrectDrinkTypeName.Reason.MoreThanMaxChars(
                DRINK_TYPE_NAME_MAX_CHARS,
            )
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

data class CorrectDrinkTypeName(
    override val data: String,
) : ValidatedDrinkTypeName()

data class IncorrectDrinkTypeName(
    override val data: String,
    val reason: Reason,
) : ValidatedDrinkTypeName() {
    sealed class Reason {
        data object Empty : Reason()
        data class MoreThanMaxChars(val maxChars: Int) : Reason()
    }
}