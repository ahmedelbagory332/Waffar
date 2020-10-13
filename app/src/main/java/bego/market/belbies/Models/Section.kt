package bego.market.belbies.Models

data class Section(
    val sections: MutableList<Sections>
)

data class Sections(
    val id: String,
    val image: String,
    val name: String
)