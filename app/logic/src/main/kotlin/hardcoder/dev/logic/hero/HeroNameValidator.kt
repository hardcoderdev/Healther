package hardcoder.dev.logic.hero

class HeroNameValidator {

    fun validate(heroName: String): ValidatedHeroName {
        return heroName.incorrectReason()?.let { reason ->
            IncorrectHeroName(data = heroName, reason = reason)
        } ?: run {
            CorrectHeroName(data = heroName)
        }
    }

    private fun String.incorrectReason(): IncorrectHeroName.Reason? {
        return when {
            isBlank() -> IncorrectHeroName.Reason.Empty
            length > HERO_NAME_MAX_CHARS -> IncorrectHeroName.Reason.MoreThanMaxChars(
                HERO_NAME_MAX_CHARS,
            )
            else -> null
        }
    }

    private companion object {
        private const val HERO_NAME_MAX_CHARS = 30
    }
}

sealed class ValidatedHeroName {
    abstract val data: String
}

class CorrectHeroName(
    override val data: String,
) : ValidatedHeroName()

class IncorrectHeroName(
    override val data: String,
    val reason: Reason,
) : ValidatedHeroName() {
    sealed class Reason {
        object Empty : Reason()
        data class MoreThanMaxChars(val maxChars: Int) : Reason()
    }
}
