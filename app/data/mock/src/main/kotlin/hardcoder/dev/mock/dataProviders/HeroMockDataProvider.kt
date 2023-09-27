package hardcoder.dev.mock.dataProviders

import hardcoder.dev.logic.hero.Hero
import hardcoder.dev.logic.hero.gender.Gender

object HeroMockDataProvider {

    fun hero() = Hero(
        id = 0,
        name = "Dragon slayer",
        experiencePoints = 0f,
        exerciseStressTime = 2,
        weight = 60,
        level = 2,
        gender = Gender.MALE,
        coins = 20f,
        crystals = 10,
        currentHealthPoints = 50,
        maxHealthPoints = 100,
    )
}