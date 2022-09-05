package bego.market.waffar.models

data class AllProductModel(
    val allProducts: MutableList<AllProduct>,
)

data class AllProduct(
    val id: String,
    val name: String,
    val image: String,
    val description: String,
    val price: String,
    val section: String,
    val productOfferPrice: String,
    val productOfferPercentage: String,
)
