package hardcoder.dev.logic.currency

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import kotlinx.coroutines.withContext

class WalletCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun create(ownerId: Int) = withContext(dispatchers.io) {
        appDatabase.walletQueries.create(
            id = idGenerator.nextId(),
            ownerId = ownerId,
            capacity = WALLET_INITIAL_CAPACITY,
            coins = WALLET_INITIAL_COINS,
            crystals = WALLET_INITIAL_CRYSTALS,
        )
    }
}
