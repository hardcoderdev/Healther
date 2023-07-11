package hardcoder.dev.logic.currency

import app.cash.sqldelight.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.Wallet
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import hardcoder.dev.logic.currency.Wallet as WalletEntity

class WalletProvider(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideWalletByOwnerId(userId: Int) = appDatabase.walletQueries
        .provideUserWallet(ownerId = userId)
        .asFlow()
        .map { it.executeAsOneOrNull()?.toEntity() }
        .flowOn(dispatchers.io)

    private fun Wallet.toEntity() = WalletEntity(
        capacity = capacity,
        coins = coins,
        crystals = crystals,
    )
}