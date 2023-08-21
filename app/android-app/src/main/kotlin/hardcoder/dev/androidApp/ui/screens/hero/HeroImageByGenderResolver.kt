package hardcoder.dev.androidApp.ui.screens.hero

import hardcoder.dev.logic.hero.gender.Gender
import hardcoderdev.healther.app.android.app.R

class HeroImageByGenderResolver {

    // TODO PLACE HERE REAL IMAGES
    fun resolve(gender: Gender) = when (gender) {
        Gender.MALE -> R.drawable.sample_image_hero
        Gender.FEMALE -> R.drawable.gender_female
    }
}