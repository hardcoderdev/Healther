package hardcoder.dev.di.presentation

import hardcoder.dev.presentation.splash.SplashViewModel
import hardcoder.dev.presentation.user.UserCreationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val heroPresentationModule = module {
    single {
        SplashViewModel(
            appPreferenceProvider = get(),
        )
    }

    single {
        UserCreationViewModel(
            userCreator = get(),
            appPreferenceUpdater = get(),
            userGenderProvider = get(),
            dateTimeProvider = get(),
            userNameValidator = get(),
            userWeightValidator = get(),
            userExerciseStressValidator = get(),
        )
    }
}