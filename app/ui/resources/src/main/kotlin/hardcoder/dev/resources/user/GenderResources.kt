package hardcoder.dev.resources.user

import hardcoder.dev.entities.user.Gender

data class GenderResources(
    val nameResId: Int,
    val imageResId: Int,
    val gender: Gender,
)