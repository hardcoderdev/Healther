package hardcoder.dev.healther.ui.screens.setUpFlow.heroCreate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.healther.entities.Gender
import hardcoder.dev.healther.ui.base.LocalPresentationModule

@Composable
fun HeroCreateScreen(
    gender: Gender,
    weight: Int,
    exerciseStressTime: Int,
    onGoToDashboard: () -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val heroCreateViewModel = viewModel {
        presentationModule.createHeroCreateViewModel()
    }

    LaunchedEffect(key1 = Unit) {
        heroCreateViewModel.createHero(
            gender,
            weight,
            exerciseStressTime
        )
        onGoToDashboard()
    }
}