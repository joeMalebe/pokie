package co.za.pokie.domain.model

data class PokemonData(
    val name: String,
    val url: String,
)

data class Pokemon(
    val name: String,
    val image: String,
    val weight: Int,
    val height: Int,
    val baseExperience: Int,
    val abilities: List<String> = listOf(),
    val stats: List<Stat> = listOf(),
    val type: List<String> = listOf(),
)

data class Stat(
    val name: String,
    val value: Float,
)

data class PageData(val currentPage:Int, val isLastPage:Boolean, val pokemons: List<Pokemon>)

data class HomeViewState(
    val pokemonList: List<Pokemon> = listOf(),
    val filteredList: List<Pokemon> = listOf(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isPageLoading: Boolean = false,
    val isDataLoaded: Boolean = false,
    val errorHeading: String? = null,
    val errorDescription: String? = null,
)
