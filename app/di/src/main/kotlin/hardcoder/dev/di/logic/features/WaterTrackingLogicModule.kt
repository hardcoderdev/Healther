package hardcoder.dev.di.logic.features

import hardcoder.dev.icons.IconResourceProvider
import hardcoder.dev.logics.features.waterTracking.WaterTrackCreator
import hardcoder.dev.logics.features.waterTracking.WaterTrackDeleter
import hardcoder.dev.logics.features.waterTracking.WaterTrackProvider
import hardcoder.dev.logics.features.waterTracking.WaterTrackUpdater
import hardcoder.dev.logics.features.waterTracking.WaterTrackingDailyRateProvider
import hardcoder.dev.logics.features.waterTracking.WaterTrackingMillilitersDrunkProvider
import hardcoder.dev.logics.features.waterTracking.WaterTrackingStatisticProvider
import hardcoder.dev.logics.features.waterTracking.drinkType.DrinkTypeCreator
import hardcoder.dev.logics.features.waterTracking.drinkType.DrinkTypeDeleter
import hardcoder.dev.logics.features.waterTracking.drinkType.DrinkTypeProvider
import hardcoder.dev.logics.features.waterTracking.drinkType.DrinkTypeUpdater
import hardcoder.dev.logics.features.waterTracking.drinkType.PredefinedDrinkTypeProvider
import hardcoder.dev.resolvers.features.waterTracking.WaterIntakeResolver
import hardcoder.dev.resolvers.features.waterTracking.WaterPercentageResolver
import hardcoder.dev.resources.features.waterTracking.DrinkTypeIconProvider
import hardcoder.dev.resources.features.waterTracking.PredefinedDrinkTypeProviderImpl
import hardcoder.dev.validators.features.waterTracking.DrinkTypeNameValidator
import hardcoder.dev.validators.features.waterTracking.WaterTrackMillilitersValidator
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val waterTrackingLogicModule = module {
    singleOf(::WaterPercentageResolver)
    singleOf(::WaterIntakeResolver)
    singleOf(::WaterTrackMillilitersValidator)
    singleOf(::DrinkTypeNameValidator)
    singleOf<IconResourceProvider>(::DrinkTypeIconProvider)

    single {
        WaterTrackingDailyRateProvider(
            waterIntakeResolver = get(),
            userProvider = get(),
            dispatchers = get(),
            genderIdMapper = get(),
        )
    }

    single {
        WaterTrackingMillilitersDrunkProvider(
            waterTrackProvider = get(),
            waterPercentageResolver = get(),
            waterTrackingDailyRateProvider = get(),
            dispatchers = get(),
        )
    }

    single {
        WaterTrackCreator(
            idGenerator = get(),
            appDatabase = get(),
            dispatchers = get(),
        )
    }

    single {
        WaterTrackUpdater(
            appDatabase = get(),
            dispatchers = get(),
        )
    }

    single {
        WaterTrackDeleter(
            appDatabase = get(),
            dispatchers = get(),
        )
    }

    single {
        WaterTrackProvider(
            appDatabase = get(),
            drinkTypeProvider = get(),
            dispatchers = get(),
        )
    }

    single {
        WaterTrackingStatisticProvider(
            appDatabase = get(),
            drinkTypeProvider = get(),
            dispatchers = get(),
        )
    }

    single {
        DrinkTypeCreator(
            idGenerator = get(),
            appDatabase = get(),
            predefinedDrinkTypeProvider = get(),
            dispatchers = get(),
        )
    }

    single {
        DrinkTypeUpdater(
            appDatabase = get(),
            dispatchers = get(),
        )
    }

    single {
        DrinkTypeDeleter(
            appDatabase = get(),
            waterTrackDeleter = get(),
            dispatchers = get(),
        )
    }

    single {
        DrinkTypeProvider(
            appDatabase = get(),
            iconResourceProvider = get(),
            dispatchers = get(),
        )
    }

    single<PredefinedDrinkTypeProvider> {
        PredefinedDrinkTypeProviderImpl(
            context = androidContext(),
            iconResourceProvider = get(),
        )
    }
}