package hardcoder.dev.logic.reward.currency

import kotlinx.datetime.Instant

data class Currency(
    val id: Int,
    val date: Instant,
    val currencyType: CurrencyType,
    val amount: Float,
    val isCollected: Boolean,
)