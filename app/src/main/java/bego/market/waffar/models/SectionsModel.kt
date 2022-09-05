package bego.market.waffar.models



data class SectionsModel(
    val sections: MutableList<Section>,
)

data class Section(
    val id: String,
    val name: String,
    val image: String,
)
