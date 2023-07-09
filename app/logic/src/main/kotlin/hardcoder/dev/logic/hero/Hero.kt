package hardcoder.dev.logic.hero

import hardcoder.dev.logic.hero.gender.Gender

data class Hero(
    val weight: Int,
    val exerciseStressTime: Int,
    val gender: Gender,
)