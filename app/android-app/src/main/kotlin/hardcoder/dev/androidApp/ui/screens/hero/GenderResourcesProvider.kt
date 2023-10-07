package hardcoder.dev.androidApp.ui.screens.hero

import hardcoder.dev.logic.hero.gender.Gender
import hardcoderdev.healther.app.resources.R

class GenderResourcesProvider {

    private val map = mapOf(
        Gender.MALE to listOf(
            R.string.hero_gender_male,
            R.drawable.gender_male,
        ),
        Gender.FEMALE to listOf(
            R.string.hero_gender_female,
            R.drawable.gender_female,
        ),
    )

    fun provide(gender: Gender) = checkNotNull(map[gender]).let {
        GenderResources(
            nameResId = it[0],
            imageResId = it[1],
            gender = gender,
        )
    }
}