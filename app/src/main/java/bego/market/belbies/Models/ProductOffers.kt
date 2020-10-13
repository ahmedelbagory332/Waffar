package bego.market.belbies.Models

data class ProductOffers(
    var offers: MutableList<Offer>
)

data class Offer(
    val description: String,
    val id: String,
    val image: String,
    val name: String,
    val price: String,
    val productOfferPercentage: String,
    val productOfferPrice: String,
    val section: String
)