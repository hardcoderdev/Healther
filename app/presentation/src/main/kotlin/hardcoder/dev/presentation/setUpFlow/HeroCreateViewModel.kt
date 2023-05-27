package hardcoder.dev.presentation.setUpFlow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.logic.appPreferences.AppPreference
import hardcoder.dev.logic.appPreferences.AppPreferenceUpdater
import hardcoder.dev.logic.hero.HeroCreator
import hardcoder.dev.logic.hero.gender.Gender
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class HeroCreateViewModel(
    appPreferenceUpdater: AppPreferenceUpdater,
    heroCreator: HeroCreator,
    gender: Gender,
    weight: Int,
    exerciseStressTime: Int
) : ViewModel() {

    val creationController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            heroCreator.createHero(
                weight = weight,
                exerciseStressTime = exerciseStressTime,
                gender = gender
            )

            appPreferenceUpdater.update(
                AppPreference(
                    firstLaunchTime = Clock.System.now(),
                    lastAppReviewRequestTime = null
                )
            )
        }
    )
}