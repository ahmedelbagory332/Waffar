package bego.market.belbies.Models

data class AdminProducts(
    val allProducts: MutableList<AllProducts>
)

data class AllProducts(
    val description: String,
    val id: String,
    val image: String,
    val name: String,
    val price: String,
    val productOfferPercentage: String,
    val productOfferPrice: String,
    val section: String

)