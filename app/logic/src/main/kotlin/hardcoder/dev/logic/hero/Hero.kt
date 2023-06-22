package hardcoder.dev.logic.hero

import hardcoder.dev.logic.hero.gender.Gender

internal const val HERO_ID = 0

data class Hero(
    val weight: Int,
    val exerciseStressTime: Int,
    val gender: Gender
)
