package hardcoder.dev.database.appAdapters

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import hardcoder.dev.database.Wallet

object WalletAdapter {

    fun createWalletAdapter() = Wallet.Adapter(
        idAdapter = IntColumnAdapter,
        ownerIdAdapter = IntColumnAdapter,
        coinsAdapter = IntColumnAdapter,
        crystalsAdapter = IntColumnAdapter,
        capacityAdapter = IntColumnAdapter,
    )
}
