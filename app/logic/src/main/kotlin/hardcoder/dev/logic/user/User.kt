package hardcoder.dev.logic.user

import hardcoder.dev.logic.user.gender.Gender

data class User(
    val id: Int,
    val weight: Int,
    val exerciseStressTime: Int,
    val gender: Gender,
    val name: String,
)