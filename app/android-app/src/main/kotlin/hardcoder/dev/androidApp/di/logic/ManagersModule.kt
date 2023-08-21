package hardcoder.dev.androidApp.di.logic

import hardcoder.dev.logic.appPreferences.LastEntranceManager
import hardcoder.dev.logic.reward.penalty.PenaltyManager
import hardcoder.dev.logic.hero.health.HeroHealthPointsManager
import org.koin.dsl.module

val managersModule = module {
    single {
        HeroHealthPointsManager(
            appDatabase = get(),
            dispatchers = get(),
            heroProvider = get(),
        )
    }

    single {
        PenaltyManager(
            dateTimeProvider = get(),
            penaltyCollector = get(),
            featureTypeProvider = get(),
            waterTrackingPenaltyMaker = get(),
            pedometerPenaltyMaker = get(),
            moodTrackingPenaltyMaker = get(),
            diaryPenaltyMaker = get(),
            fastingPenaltyMaker = get(),
        )
    }

    single {
        LastEntranceManager(
            appPreferenceUpdater = get(),
            appPreferenceProvider = get(),
            dateTimeProvider = get(),
        )
    }
}