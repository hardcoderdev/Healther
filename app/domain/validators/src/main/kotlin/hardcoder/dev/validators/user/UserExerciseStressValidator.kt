package hardcoder.dev.validators.user

class UserExerciseStressValidator {

    fun validate(userExerciseStressTime: String): ValidatedUserExerciseStressTime {
        return userExerciseStressTime.incorrectReason()?.let { reason ->
            IncorrectUserExerciseStressTime(data = userExerciseStressTime, reason = reason)
        } ?: run {
            CorrectUserExerciseStressTime(data = userExerciseStressTime)
        }
    }

    private fun String.incorrectReason(): IncorrectUserExerciseStressTime.Reason? {
        return when {
            isBlank() -> IncorrectUserExerciseStressTime.Reason.Empty
            this.contains(",") || this.contains(".") -> IncorrectUserExerciseStressTime.Reason.InvalidCharsInWeight(
                invalidChars = charArrayOf(',', '.'),
            )

            toInt() < USER_MINIMUM_EXERCISE_STRESS_TIME -> IncorrectUserExerciseStressTime.Reason.LessThanMinimum(
                USER_MINIMUM_EXERCISE_STRESS_TIME,
            )

            toInt() > USER_MAXIMUM_EXERCISE_STRESS_TIME -> IncorrectUserExerciseStressTime.Reason.MoreThanMaximum(
                USER_MAXIMUM_EXERCISE_STRESS_TIME,
            )
            else -> null
        }
    }

    private companion object {
        private const val USER_MINIMUM_EXERCISE_STRESS_TIME = 0
        private const val USER_MAXIMUM_EXERCISE_STRESS_TIME = 24
    }
}

sealed class ValidatedUserExerciseStressTime {
    abstract val data: String
}

class CorrectUserExerciseStressTime(
    override val data: String,
) : ValidatedUserExerciseStressTime()

class IncorrectUserExerciseStressTime(
    override val data: String,
    val reason: Reason,
) : ValidatedUserExerciseStressTime() {
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
