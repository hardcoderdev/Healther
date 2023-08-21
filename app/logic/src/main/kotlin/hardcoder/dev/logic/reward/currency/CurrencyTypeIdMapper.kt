package hardcoder.dev.logic.reward.currency

class CurrencyTypeIdMapper {

    private val map = mapOf(
        CurrencyType.COINS to COINS_CURRENCY_TYPE_ID,
        CurrencyType.CRYSTALS to CRYSTALS_CURRENCY_TYPE_ID,
    )

    fun mapToId(currencyType: CurrencyType) = checkNotNull(map[currencyType])

    fun mapToCurrencyType(currencyTypeId: Int) = checkNotNull(
        map.entries.find { it.value == currencyTypeId },
    ).key

    private companion object {
        private const val COINS_CURRENCY_TYPE_ID = 0
        private const val CRYSTALS_CURRENCY_TYPE_ID = 1
    }
}