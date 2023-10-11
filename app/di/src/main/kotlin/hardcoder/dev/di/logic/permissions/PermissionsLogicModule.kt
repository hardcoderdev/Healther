package hardcoder.dev.di.logic.permissions

import hardcoder.dev.pedometer_manager.BatteryRequirementsController
import hardcoder.dev.permissions.PermissionsController
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val permissionsLogicModule = module {
    singleOf(::BatteryRequirementsController)
    singleOf(::PermissionsController)
}