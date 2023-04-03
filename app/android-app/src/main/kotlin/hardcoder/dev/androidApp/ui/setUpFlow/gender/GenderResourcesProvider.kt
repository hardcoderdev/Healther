package hardcoder.dev.androidApp.ui.setUpFlow.gender

import hardcoder.dev.healther.R
import hardcoder.dev.logic.entities.hero.Gender

class GenderResourcesProvider {

    private val map = mapOf(
        Gender.MALE to listOf(
            R.string.gender_male,
            R.drawable.gender_male
        ),
        Gender.FEMALE to listOf(
            R.string.gender_female,
            R.drawable.gender_female
        )
    )

    fun provide(gender: Gender) = checkNotNull(map[gender]).let {
        GenderResources(
            nameResId = it[0],
            imageResId = it[1],
            gender = gender
        )
    }
}