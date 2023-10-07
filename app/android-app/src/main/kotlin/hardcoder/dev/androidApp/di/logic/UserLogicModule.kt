package hardcoder.dev.androidApp.di.logic

import hardcoder.dev.logic.user.UserCreator
import hardcoder.dev.logic.user.UserExerciseStressValidator
import hardcoder.dev.logic.user.UserNameValidator
import hardcoder.dev.logic.user.UserProvider
import hardcoder.dev.logic.user.UserUpdater
import hardcoder.dev.logic.user.UserWeightValidator
import hardcoder.dev.logic.user.gender.GenderIdMapper
import hardcoder.dev.logic.user.gender.GenderProvider
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val heroLogicModule = module {
    singleOf(::GenderProvider)
    singleOf(::GenderIdMapper)
    singleOf(::UserNameValidator)
    singleOf(::UserWeightValidator)
    singleOf(::UserExerciseStressValidator)

    single {
        UserCreator(
            appDatabase = get(),
            genderIdMapper = get(),
            dispatchers = get(),
            idGenerator = get(),
        )
    }

    single {
        UserUpdater(
            appDatabase = get(),
            genderIdMapper = get(),
            dispatchers = get(),
        )
    }

    single {
        UserProvider(
            appDatabase = get(),
            genderIdMapper = get(),
            dispatchers = get(),
        )
    }
}