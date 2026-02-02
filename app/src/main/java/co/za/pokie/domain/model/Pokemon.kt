package co.za.pokie.domain.model

data class Pokemon(val name: String,val url: String)
data class Pokemon1(
    val name: String,
    val image: String,
    val weight: Int,
    val height: Int,
    val baseExperience: Int
)

data class HomeViewData(
    val pokemonList: List<Pokemon1> = listOf(),
    val isLoading: Boolean = false,
    val errorHeading: String? = null,
    val errorDescription: String? = null
)




