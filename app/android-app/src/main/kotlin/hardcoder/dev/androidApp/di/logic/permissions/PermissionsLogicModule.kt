package hardcoder.dev.androidApp.di.logic.permissions

import hardcoder.dev.androidApp.ui.screens.features.pedometer.logic.BatteryRequirementsController
import hardcoder.dev.permissions.PermissionsController
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val permissionsLogicModule = module {
    singleOf(::BatteryRequirementsController)
    singleOf(::PermissionsController)
}