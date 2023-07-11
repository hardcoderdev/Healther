package hardcoder.dev.logic.currency

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.withContext

class WalletUpdater(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun update(
        capacity: Int,
        coins: Int,
        crystals: Int,
    ) = withContext(dispatchers.io) {
        appDatabase.walletQueries.update(
            capacity = capacity,
            coins = coins,
            crystals = crystals,
        )
    }
}
