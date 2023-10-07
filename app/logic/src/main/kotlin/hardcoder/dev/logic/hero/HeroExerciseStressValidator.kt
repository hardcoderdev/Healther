package hardcoder.dev.logic.hero

class HeroExerciseStressValidator {

    fun validate(heroExerciseStressTime: String): ValidatedHeroExerciseStress {
        return heroExerciseStressTime.incorrectReason()?.let { reason ->
            IncorrectHeroHeroExerciseStress(data = heroExerciseStressTime, reason = reason)
        } ?: run {
            CorrectHeroHeroExerciseStress(data = heroExerciseStressTime)
        }
    }

    private fun String.incorrectReason(): IncorrectHeroHeroExerciseStress.Reason? {
        return when {
            isBlank() -> IncorrectHeroHeroExerciseStress.Reason.Empty
            this.contains(",") || this.contains(".") -> IncorrectHeroHeroExerciseStress.Reason.InvalidCharsInWeight(
                invalidChars = charArrayOf(',', '.'),
            )
            toInt() < HERO_MINIMUM_EXERCISE_STRESS_TIME -> IncorrectHeroHeroExerciseStress.Reason.LessThanMinimum(
                HERO_MINIMUM_EXERCISE_STRESS_TIME,
            )
            toInt() > HERO_MAXIMUM_EXERCISE_STRESS_TIME -> IncorrectHeroHeroExerciseStress.Reason.MoreThanMaximum(
                HERO_MAXIMUM_EXERCISE_STRESS_TIME,
            )
            else -> null
        }
    }

    private companion object {
        private const val HERO_MINIMUM_EXERCISE_STRESS_TIME = 0
        private const val HERO_MAXIMUM_EXERCISE_STRESS_TIME = 24
    }
}

sealed class ValidatedHeroExerciseStress {
    abstract val data: String
}

class CorrectHeroHeroExerciseStress(
    override val data: String,
) : ValidatedHeroExerciseStress()

class IncorrectHeroHeroExerciseStress(
    override val data: String,
    val reason: Reason,
) : ValidatedHeroExerciseStress() {
    sealed class Reason {
        object Empty : Reason()
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
