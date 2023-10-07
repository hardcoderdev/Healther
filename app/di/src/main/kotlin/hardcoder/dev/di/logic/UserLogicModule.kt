package hardcoder.dev.di.logic

import hardcoder.dev.logic.user.UserCreator
import hardcoder.dev.logic.user.UserGenderProvider
import hardcoder.dev.logic.user.UserProvider
import hardcoder.dev.logic.user.UserUpdater
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