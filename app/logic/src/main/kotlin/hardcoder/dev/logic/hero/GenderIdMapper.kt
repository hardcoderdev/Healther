package hardcoder.dev.logic.hero

import hardcoder.dev.entities.Gender

class GenderIdMapper {

    fun mapToId(gender: Gender) = when (gender) {
        Gender.MALE -> 0
        Gender.FEMALE -> 1
    }

    fun mapToGender(id: Int) = when (id) {
        0 -> Gender.MALE
        1 -> Gender.FEMALE
        else -> Gender.MALE
    }
}