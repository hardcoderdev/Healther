package hardcoder.dev.androidApp.di.logic

import hardcoder.dev.logic.dailyStreak.DailyStreakCreator
import hardcoder.dev.logic.dailyStreak.DailyStreakProvider
import hardcoder.dev.logic.dailyStreak.DailyStreakUpdater
import org.koin.dsl.module

val dailyStreakLogicModule = module {

    single {
        DailyStreakCreator(
            appDatabase = get(),
            idGenerator = get(),
            dispatchers = get(),
            dateTimeProvider = get(),
        )
    }

    single {
        DailyStreakUpdater(
            appDatabase = get(),
            dispatchers = get(),
            dateTimeProvider = get(),
        )
    }

    single {
        DailyStreakProvider(
            appDatabase = get(),
            dispatchers = get(),
        )
    }
}