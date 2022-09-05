package bego.market.waffar.models

data class UsersOrdersModel(
    val UsersOrders: MutableList<UsersOrder>,
)

data class UsersOrder(
    val mail: String,
    val orderNumbers: String,
    val userAddress: String,
    val userPhone: String,
    val userName: String,
)