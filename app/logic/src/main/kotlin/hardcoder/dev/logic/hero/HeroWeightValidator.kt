package hardcoder.dev.logic.hero

class HeroWeightValidator {

    fun validate(heroWeight: String): ValidatedHeroWeight {
        return heroWeight.incorrectReason()?.let { reason ->
            IncorrectHeroWeight(data = heroWeight, reason = reason)
        } ?: run {
            CorrectHeroWeight(data = heroWeight)
        }
    }

    private fun String.incorrectReason(): IncorrectHeroWeight.Reason? {
        return when {
            isBlank() -> IncorrectHeroWeight.Reason.Empty
            this.contains(",") || this.contains(".") -> IncorrectHeroWeight.Reason.InvalidCharsInWeight(
                invalidChars = charArrayOf(',', '.'),
            )
            toInt() < HERO_MINIMUM_WEIGHT -> IncorrectHeroWeight.Reason.LessThanMinimum(
                HERO_MINIMUM_WEIGHT,
            )
            toInt() > HERO_MAXIMUM_WEIGHT -> IncorrectHeroWeight.Reason.MoreThanMaximum(
                HERO_MAXIMUM_WEIGHT,
            )
            else -> null
        }
    }

    private companion object {
        private const val HERO_MINIMUM_WEIGHT = 30
        private const val HERO_MAXIMUM_WEIGHT = 400
    }
}

sealed class ValidatedHeroWeight {
    abstract val data: String
}

class CorrectHeroWeight(
    override val data: String,
) : ValidatedHeroWeight()

class IncorrectHeroWeight(
    override val data: String,
    val reason: Reason,
) : ValidatedHeroWeight() {
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
