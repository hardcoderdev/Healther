package hardcoder.dev.androidApp.di.logic

import hardcoder.dev.logic.hero.HeroCreator
import hardcoder.dev.logic.hero.HeroProvider
import hardcoder.dev.logic.hero.HeroUpdater
import hardcoder.dev.logic.hero.gender.GenderIdMapper
import hardcoder.dev.logic.hero.HeroNameValidator
import hardcoder.dev.logic.hero.gender.GenderProvider
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val heroLogicModule = module {
    singleOf(::GenderProvider)
    singleOf(::GenderIdMapper)
    singleOf(::HeroNameValidator)

    single {
        HeroCreator(
            appDatabase = get(),
            genderIdMapper = get(),
            dispatchers = get(),
            idGenerator = get(),
        )
    }

    single {
        HeroUpdater(
            appDatabase = get(),
            genderIdMapper = get(),
            dispatchers = get(),
        )
    }

    single {
        HeroProvider(
            appDatabase = get(),
            genderIdMapper = get(),
            dispatchers = get(),
        )
    }
}