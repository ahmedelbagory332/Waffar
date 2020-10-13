package bego.market.belbies.Models

data class UserOrders(
    val ordersUser: MutableList<OrdersUser>
)

data class OrdersUser(
    val id: String,
    val image: String,
    val name: String,
    val price: String,
    val productNumber: String,
    val productUserEmail: String
)