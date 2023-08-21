package hardcoder.dev.androidApp.di.logic.currency

import hardcoder.dev.logic.reward.currency.CurrencyCalculator
import hardcoder.dev.logic.reward.currency.CurrencyCollector
import hardcoder.dev.logic.reward.currency.CurrencyProvider
import hardcoder.dev.logic.reward.currency.CurrencyCreator
import hardcoder.dev.logic.reward.currency.CurrencyTypeIdMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val currencyLogicModule = module {
    singleOf(::CurrencyCalculator)
    singleOf(::CurrencyTypeIdMapper)

    single {
        CurrencyCreator(
            appDatabase = get(),
            idGenerator = get(),
            dispatchers = get(),
            currencyTypeIdMapper = get(),
            featureTypeIdMapper = get(),
        )
    }

    single {
        CurrencyCollector(
            appDatabase = get(),
            dispatchers = get(),
            featureTypeIdMapper = get(),
            currencyTypeIdMapper = get(),
            heroProvider = get(),
            dateTimeProvider = get(),
        )
    }

    single {
        CurrencyProvider(
            appDatabase = get(),
            dispatchers = get(),
            currencyTypeIdMapper = get(),
            featureTypeIdMapper = get(),
        )
    }
}