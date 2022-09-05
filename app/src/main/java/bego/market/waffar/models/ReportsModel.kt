package bego.market.waffar.models

data class ReportsModel(
    val reports: MutableList<Reports>,
)

data class Reports(
    val id: String,
    val userReport: String,
    val textReport: String,
)
