package hardcoder.dev.logic.hero.gender

class GenderIdMapper {

    fun mapToId(gender: Gender) = when (gender) {
        Gender.MALE -> 0
        Gender.FEMALE -> 1
    }

    fun mapToGender(id: Int) = when (id) {
        0 -> Gender.MALE
        1 -> Gender.FEMALE
        else -> throw IllegalStateException("This enum member not found")
    }
}