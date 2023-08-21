package hardcoder.dev.logic.hero.gender

class GenderIdMapper {

    private val map = mapOf(
        Gender.MALE to GENDER_MALE_ID,
        Gender.FEMALE to GENDER_FEMALE_ID,
    )

    fun mapToId(gender: Gender) = checkNotNull(map[gender])

    fun mapToGender(genderId: Int) = checkNotNull(
        map.entries.find { it.value == genderId },
    ).key

    private companion object {
        private const val GENDER_MALE_ID = 0
        private const val GENDER_FEMALE_ID = 1
    }
}