package hardcoder.dev.androidApp.di.logic.experience

import hardcoder.dev.logic.reward.experience.ExperienceCreator
import hardcoder.dev.logic.reward.experience.ExperienceProvider
import hardcoder.dev.logic.reward.experience.ExperienceCalculator
import hardcoder.dev.logic.reward.experience.ExperienceCollector
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val experienceLogicModule = module {
    singleOf(::ExperienceCalculator)
    single {
        ExperienceCollector(
            heroProvider = get(),
            appDatabase = get(),
            dispatchers = get(),
            featureTypeIdMapper = get(),
            heroExperiencePointsProvider = get(),
            dateTimeProvider = get(),
        )
    }

    single {
        ExperienceCreator(
            appDatabase = get(),
            idGenerator = get(),
            dispatchers = get(),
            featureTypeIdMapper = get(),
        )
    }

    single {
        ExperienceProvider(
            appDatabase = get(),
            dispatchers = get(),
            featureTypeIdMapper = get(),
        )
    }
}