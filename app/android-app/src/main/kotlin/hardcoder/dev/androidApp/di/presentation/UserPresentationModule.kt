package hardcoder.dev.androidApp.di.presentation

import hardcoder.dev.presentation.splash.SplashViewModel
import hardcoder.dev.presentation.user.UserCreationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val heroPresentationModule = module {
    viewModel {
        SplashViewModel(
            appPreferenceProvider = get(),
        )
    }

    viewModel {
        UserCreationViewModel(
            userCreator = get(),
            appPreferenceUpdater = get(),
            genderProvider = get(),
            dateTimeProvider = get(),
            userNameValidator = get(),
            userWeightValidator = get(),
            userExerciseStressValidator = get(),
        )
    }
}