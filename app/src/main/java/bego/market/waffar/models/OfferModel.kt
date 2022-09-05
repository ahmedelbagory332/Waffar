package bego.market.waffar.models


data class OfferModel(
    val offers: MutableList<Offer>,
)

data class Offer(
    val id: String,
    val name: String,
    val image: String,
    val description: String,
    val price: String,
    val section: String,
    val productOfferPrice: String,
    val productOfferPercentage: String,
)
