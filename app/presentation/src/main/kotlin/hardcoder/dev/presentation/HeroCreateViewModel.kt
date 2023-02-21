package hardcoder.dev.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.entities.Gender
import hardcoder.dev.logic.creators.AppPreferenceCreator
import hardcoder.dev.logic.creators.HeroCreator
import kotlinx.coroutines.launch

class HeroCreateViewModel(
    private val appPreferenceCreator: AppPreferenceCreator,
    private val heroCreator: HeroCreator
) : ViewModel() {

    suspend fun createHero(
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
        }

        updateFirstLaunch()
    }

    private suspend fun updateFirstLaunch() {
        viewModelScope.launch {
            appPreferenceCreator.createAppPreference()
        }
    }
}