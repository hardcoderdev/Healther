package hardcoder.dev.androidApp.di.presentation

import hardcoder.dev.presentation.setUpFlow.EnterExerciseStressTimeViewModel
import hardcoder.dev.presentation.setUpFlow.EnterWeightViewModel
import hardcoder.dev.presentation.setUpFlow.HeroCreateViewModel
import hardcoder.dev.presentation.setUpFlow.SelectGenderViewModel
import hardcoder.dev.presentation.setUpFlow.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val heroPresentationModule = module {
    viewModelOf(::EnterWeightViewModel)
    viewModelOf(::SelectGenderViewModel)
    viewModelOf(::EnterExerciseStressTimeViewModel)

    viewModel {
        SplashViewModel(
            appPreferenceProvider = get(),
        )
    }

    viewModel { parameters ->
        HeroCreateViewModel(
            heroCreator = get(),
            appPreferenceUpdater = get(),
            gender = parameters.get(),
            weight = parameters.get(),
            exerciseStressTime = parameters.get(),
        )
    }
}