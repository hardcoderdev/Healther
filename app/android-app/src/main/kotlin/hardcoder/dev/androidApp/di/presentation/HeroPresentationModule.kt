package hardcoder.dev.androidApp.di.presentation

import hardcoder.dev.presentation.hero.HeroCreationViewModel
import hardcoder.dev.presentation.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val heroPresentationModule = module {
    viewModel {
        SplashViewModel(
            appPreferenceProvider = get(),
            heroProvider = get(),
            penaltyManager = get(),
            appPreferenceUpdater = get(),
            dateTimeProvider = get(),
        )
    }

    viewModel {
        HeroCreationViewModel(
            heroCreator = get(),
            appPreferenceUpdater = get(),
            genderProvider = get(),
            dateTimeProvider = get(),
            heroNameValidator = get(),
            heroWeightValidator = get(),
            heroExerciseStressValidator = get(),
        )
    }
}