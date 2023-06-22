package hardcoder.dev.androidApp.di

import hardcoder.dev.androidApp.di.logic.appPreferencesLogicModule
import hardcoder.dev.androidApp.di.logic.dateTimeLogicModule
import hardcoder.dev.androidApp.di.logic.features.diaryLogicModule
import hardcoder.dev.androidApp.di.logic.features.fastingLogicModule
import hardcoder.dev.androidApp.di.logic.features.moodTrackingLogicModule
import hardcoder.dev.androidApp.di.logic.features.pedometerLogicModule
import hardcoder.dev.androidApp.di.logic.features.waterTrackingLogicModule
import hardcoder.dev.androidApp.di.logic.foundationLogicModule
import hardcoder.dev.androidApp.di.logic.heroLogicModule
import hardcoder.dev.androidApp.di.logic.permissions.permissionsLogicModule
import hardcoder.dev.androidApp.di.logic.predefinedLogicModule
import hardcoder.dev.androidApp.di.logic.resolversLogicModule
import org.koin.dsl.module

val logicModule = module {
    includes(
        foundationLogicModule,
        appPreferencesLogicModule,
        predefinedLogicModule,
        permissionsLogicModule,
        resolversLogicModule,
        dateTimeLogicModule,
        heroLogicModule,
        waterTrackingLogicModule,
        pedometerLogicModule,
        fastingLogicModule,
        moodTrackingLogicModule,
        diaryLogicModule,
    )
}