package hardcoder.dev.androidApp.di.logic

import hardcoder.dev.logic.currency.WalletCreator
import hardcoder.dev.logic.currency.WalletProvider
import hardcoder.dev.logic.currency.WalletUpdater
import org.koin.dsl.module

val walletLogicModule = module {
    single {
        WalletCreator(
            idGenerator = get(),
            appDatabase = get(),
            dispatchers = get(),
        )
    }

    single {
        WalletUpdater(
            appDatabase = get(),
            dispatchers = get(),
        )
    }

    single {
        WalletProvider(
            appDatabase = get(),
            dispatchers = get(),
        )
    }
}