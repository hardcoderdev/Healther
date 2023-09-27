package hardcoder.dev.androidApp.di.logic.features

import hardcoder.dev.androidApp.ui.screens.features.waterTracking.drinkType.providers.DrinkTypeIconProvider
import hardcoder.dev.androidApp.ui.screens.features.waterTracking.drinkType.providers.PredefinedDrinkTypeProviderImpl
import hardcoder.dev.logic.features.waterTracking.resolvers.WaterIntakeResolver
import hardcoder.dev.logic.features.waterTracking.resolvers.WaterPercentageResolver
import hardcoder.dev.logic.features.waterTracking.WaterTrackCreator
import hardcoder.dev.logic.features.waterTracking.WaterTrackDeleter
import hardcoder.dev.logic.features.waterTracking.validators.WaterTrackMillilitersValidator
import hardcoder.dev.logic.features.waterTracking.WaterTrackProvider
import hardcoder.dev.logic.features.waterTracking.WaterTrackUpdater
import hardcoder.dev.logic.features.waterTracking.WaterTrackingDailyRateProvider
import hardcoder.dev.logic.features.waterTracking.WaterTrackingMillilitersDrunkProvider
import hardcoder.dev.logic.features.waterTracking.WaterTrackingPenaltyMaker
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeCreator
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeDeleter
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeNameValidator
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeProvider
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeUpdater
import hardcoder.dev.logic.features.waterTracking.drinkType.PredefinedDrinkTypeProvider
import hardcoder.dev.logic.features.waterTracking.statistic.WaterTrackingStatisticProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val waterTrackingLogicModule = module {
    singleOf(::WaterPercentageResolver)
    singleOf(::WaterIntakeResolver)
    singleOf(::WaterTrackMillilitersValidator)
    singleOf(::DrinkTypeNameValidator)
    singleOf(::DrinkTypeNameValidator)
    singleOf<hardcoder.dev.icons.IconResourceProvider>(::DrinkTypeIconProvider)

    single {
        WaterTrackingDailyRateProvider(
            waterIntakeResolver = get(),
            heroProvider = get(),
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
            currencyProvider = get(),
        )
    }

    single {
        WaterTrackingStatisticProvider(
            appDatabase = get(),
            drinkTypeProvider = get(),
            dispatchers = get(),
            currencyProvider = get(),
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

    single {
        WaterTrackingPenaltyMaker(
            millilitersDrunkProvider = get(),
            penaltyCreator = get(),
            penaltyCalculator = get(),
            heroHealthPointsManager = get(),
            dateTimeProvider = get(),
            dispatchers = get(),
            lastEntranceManager = get(),
        )
    }
}