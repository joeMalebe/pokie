package co.za.pokie.domain.model

data class Pokemon(val name: String,val url: String)
data class Pokemon1(
    val name: String,
    val image: String,
    val weight: Int,
    val height: Int,
    val baseExperience: Int,
    val abilities: List<String> = listOf(),
    val stats: List<Stat> = listOf(),
    val type: List<String> = listOf()
)

data class Stat(val name: String, val value: Float)


data class HomeViewState(
    val pokemonList: List<Pokemon1> = listOf(),
    val isLoading: Boolean = false,
    val isDataLoaded: Boolean = false,
    val errorHeading: String? = null,
    val errorDescription: String? = null
)




