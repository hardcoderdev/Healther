package hardcoder.dev.androidApp.di

import hardcoder.dev.androidApp.di.logic.appPreferencesLogicModule
import hardcoder.dev.androidApp.di.logic.currency.currencyLogicModule
import hardcoder.dev.androidApp.di.logic.penalty.penaltyLogicModule
import hardcoder.dev.androidApp.di.logic.dailyStreakLogicModule
import hardcoder.dev.androidApp.di.logic.dateTimeLogicModule
import hardcoder.dev.androidApp.di.logic.experience.experienceLogicModule
import hardcoder.dev.androidApp.di.logic.features.diaryLogicModule
import hardcoder.dev.androidApp.di.logic.features.moodTrackingLogicModule
import hardcoder.dev.androidApp.di.logic.features.pedometerLogicModule
import hardcoder.dev.androidApp.di.logic.features.waterTrackingLogicModule
import hardcoder.dev.androidApp.di.logic.foundationLogicModule
import hardcoder.dev.androidApp.di.logic.heroLogicModule
import hardcoder.dev.androidApp.di.logic.permissions.permissionsLogicModule
import hardcoder.dev.androidApp.di.logic.predefinedLogicModule
import hardcoder.dev.androidApp.di.logic.resolversLogicModule
import hardcoder.dev.androidApp.di.logic.features.fastingLogicModule
import hardcoder.dev.androidApp.di.logic.features.featuresLogicModule
import hardcoder.dev.androidApp.di.logic.managersModule
import org.koin.dsl.module

val logicModule = module {
    includes(
        foundationLogicModule,
        appPreferencesLogicModule,
        predefinedLogicModule,
        permissionsLogicModule,
        resolversLogicModule,
        managersModule,
        dateTimeLogicModule,
        heroLogicModule,
        waterTrackingLogicModule,
        pedometerLogicModule,
        fastingLogicModule,
        moodTrackingLogicModule,
        diaryLogicModule,
        featuresLogicModule,
        currencyLogicModule,
        penaltyLogicModule,
        experienceLogicModule,
        dailyStreakLogicModule,
    )
}