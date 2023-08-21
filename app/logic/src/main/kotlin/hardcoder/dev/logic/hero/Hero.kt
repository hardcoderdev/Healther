package hardcoder.dev.logic.hero

import hardcoder.dev.logic.hero.gender.Gender

data class Hero(
    val id: Int,
    val weight: Int,
    val exerciseStressTime: Int,
    val gender: Gender,
    val name: String,
    val currentHealthPoints: Int,
    val maxHealthPoints: Int,
    val coins: Float,
    val crystals: Int,
    val experiencePoints: Float,
    val level: Int,
)