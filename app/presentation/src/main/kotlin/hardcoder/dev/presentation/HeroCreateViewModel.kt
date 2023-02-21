package hardcoder.dev.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.entities.AppPreference
import hardcoder.dev.entities.Gender
import hardcoder.dev.logic.appPreferences.AppPreferenceUpdater
import hardcoder.dev.logic.hero.HeroCreator
import kotlinx.coroutines.launch

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

            appPreferenceUpdater.update(AppPreference(firstLaunchTime = System.currentTimeMillis()))
        }
    }
}