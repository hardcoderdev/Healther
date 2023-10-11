package hardcoder.dev.validators.user

class UserWeightValidator {

    fun validate(userWeight: String): ValidatedUserWeight {
        return userWeight.incorrectReason()?.let { reason ->
            IncorrectUserWeight(data = userWeight, reason = reason)
        } ?: run {
            CorrectUserWeight(data = userWeight)
        }
    }

    private fun String.incorrectReason(): IncorrectUserWeight.Reason? {
        return when {
            isBlank() -> IncorrectUserWeight.Reason.Empty
            this.contains(",") || this.contains(".") -> IncorrectUserWeight.Reason.InvalidCharsInWeight(
                invalidChars = charArrayOf(',', '.'),
            )

            toInt() < USER_MINIMUM_WEIGHT -> IncorrectUserWeight.Reason.LessThanMinimum(
                USER_MINIMUM_WEIGHT,
            )

            toInt() > USER_MAXIMUM_WEIGHT -> IncorrectUserWeight.Reason.MoreThanMaximum(
                USER_MAXIMUM_WEIGHT,
            )
            else -> null
        }
    }

    private companion object {
        private const val USER_MINIMUM_WEIGHT = 30
        private const val USER_MAXIMUM_WEIGHT = 400
    }
}

sealed class ValidatedUserWeight {
    abstract val data: String
}

class CorrectUserWeight(
    override val data: String,
) : ValidatedUserWeight()

class IncorrectUserWeight(
    override val data: String,
    val reason: Reason,
) : ValidatedUserWeight() {
    sealed class Reason {
        data object Empty : Reason()
        data class InvalidCharsInWeight(val invalidChars: CharArray) : Reason() {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false
                other as InvalidCharsInWeight
                return invalidChars.contentEquals(other.invalidChars)
            }

            override fun hashCode(): Int {
                return invalidChars.contentHashCode()
            }
        }

        data class LessThanMinimum(val minimumBoundary: Int) : Reason()
        data class MoreThanMaximum(val maximumBoundary: Int) : Reason()
    }
}
