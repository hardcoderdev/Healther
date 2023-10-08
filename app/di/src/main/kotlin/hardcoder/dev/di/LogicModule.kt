package hardcoder.dev.di

import hardcoder.dev.di.logic.appPreferencesLogicModule
import hardcoder.dev.di.logic.dateTimeLogicModule
import hardcoder.dev.di.logic.features.diaryLogicModule
import hardcoder.dev.di.logic.features.featuresLogicModule
import hardcoder.dev.di.logic.features.moodTrackingLogicModule
import hardcoder.dev.di.logic.features.pedometerLogicModule
import hardcoder.dev.di.logic.features.waterTrackingLogicModule
import hardcoder.dev.di.logic.permissions.permissionsLogicModule
import hardcoder.dev.di.logic.predefinedLogicModule
import hardcoder.dev.di.logic.resolversLogicModule
import hardcoder.dev.di.logic.userLogicModule
import org.koin.dsl.module

val logicModule = module {
    includes(
        foundationModule,
        appPreferencesLogicModule,
        predefinedLogicModule,
        permissionsLogicModule,
        resolversLogicModule,
        dateTimeLogicModule,
        userLogicModule,
        waterTrackingLogicModule,
        pedometerLogicModule,
        moodTrackingLogicModule,
        diaryLogicModule,
        featuresLogicModule,
    )
}