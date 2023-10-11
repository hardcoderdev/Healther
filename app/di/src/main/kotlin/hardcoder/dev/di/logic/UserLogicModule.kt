package hardcoder.dev.di.logic

import hardcoder.dev.logics.user.UserCreator
import hardcoder.dev.logics.user.UserGenderProvider
import hardcoder.dev.logics.user.UserProvider
import hardcoder.dev.logics.user.UserUpdater
import hardcoder.dev.mappers.user.GenderIdMapper
import hardcoder.dev.validators.user.UserExerciseStressValidator
import hardcoder.dev.validators.user.UserNameValidator
import hardcoder.dev.validators.user.UserWeightValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val userLogicModule = module {
    singleOf(::UserGenderProvider)
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