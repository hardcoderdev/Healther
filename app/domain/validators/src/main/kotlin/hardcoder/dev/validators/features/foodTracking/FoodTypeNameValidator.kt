package hardcoder.dev.validators.features.foodTracking

class FoodTypeNameValidator {

    fun validate(foodTypeName: String): ValidatedFoodTypeName {
        return foodTypeName.incorrectReason()?.let { reason ->
            IncorrectFoodTypeName(data = foodTypeName, reason = reason)
        } ?: run {
            CorrectFoodTypeName(data = foodTypeName)
        }
    }

    private fun String.incorrectReason(): IncorrectFoodTypeName.Reason? {
        return when {
            isEmpty() -> IncorrectFoodTypeName.Reason.Empty
            length > FOOD_TYPE_NAME_MAX_CHARS -> IncorrectFoodTypeName.Reason.MoreThanMaxChars(
                FOOD_TYPE_NAME_MAX_CHARS,
            )

            else -> null
        }
    }

    private companion object {
        private const val FOOD_TYPE_NAME_MAX_CHARS = 30
    }
}

sealed class ValidatedFoodTypeName {
    abstract val data: String
}

data class CorrectFoodTypeName(
    override val data: String,
) : ValidatedFoodTypeName()

data class IncorrectFoodTypeName(
    override val data: String,
    val reason: Reason,
) : ValidatedFoodTypeName() {
    sealed class Reason {
        data object Empty : Reason()
        data class MoreThanMaxChars(val maxChars: Int) : Reason()
    }
}