package co.za.pokie.data.repository

import co.za.pokie.data.network.PokieApiService
import co.za.pokie.data.util.ApiResult
import co.za.pokie.data.util.callApiClient
import co.za.pokie.data.util.mapToPokemon
import co.za.pokie.domain.model.PageData
import co.za.pokie.domain.model.Pokemon
import co.za.pokie.domain.model.PokemonData
import co.za.pokie.domain.model.PokieRepository
import io.ktor.utils.io.ioDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PokieRepositoryImpl
@Inject
constructor(
    private val client: PokieApiService,
    private val dispatcher: CoroutineDispatcher = ioDispatcher(),
) : PokieRepository {
    private var pokemons: List<PokemonData> = listOf()

    override fun getPokemons(pageNumber: Int, pageSize: Int): Flow<Result<PageData>> = flow {
        if (pokemons.isNotEmpty()) {
            loadPageData(pokemons, pageNumber, pageSize)
        } else {
            when (val pokemonList = getPokemonList()) {
                is ApiResult.Success -> {
                    pokemons = pokemonList.data
                    loadPageData(pokemons, pageNumber, pageSize)
                }

                is ApiResult.Error -> {
                    emit(Result.failure(RuntimeException(pokemonList.message)))
                }
            }
        }
    }

    private suspend fun FlowCollector<Result<PageData>>.loadPageData(
        pokemons: List<PokemonData>,
        pageNumber: Int,
        pageSize: Int,
    ) {
        val lastPage = Math.ceilDivExact(pokemons.size, pageSize)
        if (isPaginationComplete(pageNumber, lastPage)) {
            return emit(Result.success(PageData(currentPage = pageNumber, isLastPage = true, pokemons = emptyList())))
        }
        val pageRange = getRange(pageNumber, pokemons.size, pageSize)
        val pokemonDetailsList = mutableListOf<Pokemon>()
        pokemons.subList(pageRange.first, pageRange.last).forEach { data ->
            val pokemonDetail = getPokemonDetail(data.url)
            if (pokemonDetail is ApiResult.Success) {
                pokemonDetailsList.add(pokemonDetail.data)
            } else {
                return emit(Result.failure(RuntimeException((pokemonDetail as? ApiResult.Error)?.message)))
            }
        }
        val nextPage = pageNumber.inc()
        return emit(
            Result.success(
                PageData(
                    currentPage = nextPage,
                    isLastPage = isPaginationComplete(nextPage, lastPage),
                    pokemons = pokemonDetailsList,
                ),
            ),
        )
    }

    private fun isPaginationComplete(pageNumber: Int, lastPage: Int): Boolean = pageNumber > lastPage

    private fun getRange(page: Int, size: Int, itemsInPage: Int = 10): IntRange {
        val start = ((page - 1) * itemsInPage)
        val end = (page * itemsInPage).coerceAtMost(size)
        return start..end
    }

    private suspend fun getPokemonList(): ApiResult<List<PokemonData>> = withContext(dispatcher) {
        val response =
            callApiClient {
                client.getPokemonList()
            }
        when (response) {
            is ApiResult.Success -> {
                pokemons = response.data.mapToPokemon()
                ApiResult.Success(pokemons)
            }

            is ApiResult.Error -> {
                ApiResult.Error(response.message)
            }
        }
    }

    private suspend fun getPokemonDetail(url: String): ApiResult<Pokemon> = withContext(dispatcher) {
        val response =
            callApiClient {
                client.getResourceByUrl(url)
            }
        when (response) {
            is ApiResult.Success -> {
                ApiResult.Success(response.data.mapToPokemon())
            }

            is ApiResult.Error -> {
                ApiResult.Error(response.message)
            }
        }
    }
}
