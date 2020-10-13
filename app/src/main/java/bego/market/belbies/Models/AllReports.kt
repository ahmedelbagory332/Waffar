package bego.market.belbies.Models

data class AllReports(
    val reports: MutableList<Reports>
)

data class Reports(
    val id: String,
    val textReport: String,
    val userReport: String
)