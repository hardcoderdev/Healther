package hardcoder.dev.database.appAdapters

import app.cash.sqldelight.adapter.primitive.FloatColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import hardcoder.dev.database.Currency
import hardcoder.dev.database.columnAdapters.InstantAdapter

object CurrencyAdapters {

    fun createCurrencyAdapter() = Currency.Adapter(
        idAdapter = IntColumnAdapter,
        currencyAmountAdapter = FloatColumnAdapter,
        currencyTypeIdAdapter = IntColumnAdapter,
        dateAdapter = InstantAdapter,
        linkedTrackIdAdapter = IntColumnAdapter,
        featureTypeIdAdapter = IntColumnAdapter,
    )
}