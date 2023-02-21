package hardcoder.dev.android_ui.setUpFlow.heroCreate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.android_ui.LocalPresentationModule
import hardcoder.dev.entities.Gender

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