package bego.market.waffar.models


data class UserOrdersModel(
    val UserOrders: MutableList<UserOrder>,
)

data class UserOrder(
    val id: String,
    val name: String,
    val image: String,
    val productUserEmail: String,
    val price: String,
    val productNumber: String,
    val productQuantity: String,
)