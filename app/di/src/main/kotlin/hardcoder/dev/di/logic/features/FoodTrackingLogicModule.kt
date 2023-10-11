package hardcoder.dev.di.logic.features

import hardcoder.dev.icons.IconResourceProvider
import hardcoder.dev.logics.features.foodTracking.FoodTrackCreator
import hardcoder.dev.logics.features.foodTracking.FoodTrackDeleter
import hardcoder.dev.logics.features.foodTracking.FoodTrackProvider
import hardcoder.dev.logics.features.foodTracking.FoodTrackUpdater
import hardcoder.dev.logics.features.foodTracking.foodType.FoodTypeCreator
import hardcoder.dev.logics.features.foodTracking.foodType.FoodTypeDeleter
import hardcoder.dev.logics.features.foodTracking.foodType.FoodTypeProvider
import hardcoder.dev.logics.features.foodTracking.foodType.FoodTypeUpdater
import hardcoder.dev.logics.features.foodTracking.statistic.FoodTrackingStatisticProvider
import hardcoder.dev.resources.features.foodTracking.FoodTypeIconProvider
import hardcoder.dev.validators.features.foodTracking.FoodTypeNameValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val foodTrackingLogicModule = module {
    singleOf<IconResourceProvider>(::FoodTypeIconProvider)
    singleOf(::FoodTypeNameValidator)

    single {
        FoodTrackCreator(
            appDatabase = get(),
            idGenerator = get(),
            dispatchers = get(),
        )
    }

    single {
        FoodTrackUpdater(
            appDatabase = get(),
            dispatchers = get(),
        )
    }

    single {
        FoodTrackProvider(
            appDatabase = get(),
            dispatchers = get(),
            foodTypeProvider = get(),
        )
    }

    single {
        FoodTrackDeleter(
            appDatabase = get(),
            dispatchers = get(),
        )
    }

    single {
        FoodTypeCreator(
            idGenerator = get(),
            appDatabase = get(),
            dispatchers = get(),
        )
    }

    single {
        FoodTypeUpdater(
            appDatabase = get(),
            dispatchers = get(),
        )
    }

    single {
        FoodTypeDeleter(
            appDatabase = get(),
            dispatchers = get(),
        )
    }

    single {
        FoodTypeProvider(
            appDatabase = get(),
            dispatchers = get(),
            iconResourceProvider = get(),
        )
    }

    single {
        FoodTrackingStatisticProvider(
            appDatabase = get(),
            dateTimeProvider = get(),
            dispatchers = get(),
        )
    }
}