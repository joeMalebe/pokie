package co.za.pokie.data.repository

import co.za.pokie.data.network.PokieApiService
import co.za.pokie.data.util.ApiResult
import co.za.pokie.data.util.callApiClient
import co.za.pokie.data.util.mapToPokemon
import co.za.pokie.domain.model.Pokemon
import co.za.pokie.domain.model.PokemonData
import co.za.pokie.domain.model.PokieRepository
import io.ktor.utils.io.ioDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PokieRepositoryImpl
@Inject
constructor(
    private val client: PokieApiService,
    private val dispatcher: CoroutineDispatcher = ioDispatcher(),
) : PokieRepository {
    override fun getPokemons(): Flow<Result<List<Pokemon>>> = flow {
        when (val pokemonList = getPokemonList()) {
            is ApiResult.Success -> {
                val pokemonDetailsList = mutableListOf<Pokemon>()
                pokemonList.data.forEachIndexed { index, data ->
                    val pokemonDetail = getPokemonDetail(data.url)
                    if (pokemonDetail is ApiResult.Success) {
                        pokemonDetailsList.add(pokemonDetail.data)
                    }
                    if (shouldEmitResults(index, pokemonDetailsList, pokemonList)) {
                        emit(Result.success(pokemonDetailsList))
                    }
                }
            }

            is ApiResult.Error -> {
                emit(Result.failure(RuntimeException(pokemonList.message)))
            }

            is ApiResult.NoInternetError -> {
                emit(Result.failure(RuntimeException(pokemonList.message)))
            }
        }
    }

    private fun shouldEmitResults(
        index: Int,
        pokemonDetailsList: MutableList<Pokemon>,
        pokemonList: ApiResult.Success<List<PokemonData>>,
    ): Boolean = index != 0 && pokemonDetailsList.size % 5 == 0 || index == pokemonList.data.lastIndex

    private suspend fun getPokemonList(): ApiResult<List<PokemonData>> = withContext(dispatcher) {
        val response =
            callApiClient {
                client.getPokemonList()
            }
        when (response) {
            is ApiResult.Success -> {
                ApiResult.Success(response.data.mapToPokemon())
            }

            is ApiResult.Error -> {
                ApiResult.Error(response.message)
            }

            is ApiResult.NoInternetError -> {
                ApiResult.NoInternetError(response.message)
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

            is ApiResult.NoInternetError -> {
                ApiResult.NoInternetError(response.message)
            }
        }
    }
}
