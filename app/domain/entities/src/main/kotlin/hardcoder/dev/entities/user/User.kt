package hardcoder.dev.entities.user

data class User(
    val id: Int,
    val weight: Int,
    val exerciseStressTime: Int,
    val gender: Gender,
    val name: String,
)