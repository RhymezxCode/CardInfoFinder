package io.rhymezxcode.cardinfofinder.data.models


data class CardInfoPage(
    var brand: String? = null,
    var type: String? = null,
    var bank: CardBankInfo? = null,
    var country: CardCountryInfo? = null
)

