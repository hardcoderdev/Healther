package hardcoder.dev.presentation.setUpFlow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.logic.appPreferences.AppPreferenceUpdater
import hardcoder.dev.logic.entities.appPreferences.AppPreference
import hardcoder.dev.logic.entities.hero.Gender
import hardcoder.dev.logic.hero.HeroCreator
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class HeroCreateViewModel(
    private val appPreferenceUpdater: AppPreferenceUpdater,
    private val heroCreator: HeroCreator
) : ViewModel() {

    fun createUserHero(
        gender: Gender,
        weight: Int,
        exerciseStressTime: Int
    ) {
        viewModelScope.launch {
            heroCreator.createHero(
                weight = weight,
                exerciseStressTime = exerciseStressTime,
                gender = gender
            )

            appPreferenceUpdater.update(AppPreference(firstLaunchTime = Clock.System.now()))
        }
    }
}