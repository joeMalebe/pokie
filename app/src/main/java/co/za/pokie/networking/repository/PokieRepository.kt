package co.za.pokie.networking.repository

import co.za.pokie.domain.model.PokemonData
import co.za.pokie.domain.model.Pokemon
import co.za.pokie.networking.PokieApiService
import co.za.pokie.networking.util.mapToPokemon
import co.za.pokie.networking.util.ApiResult
import co.za.pokie.networking.util.callApiClient
import io.ktor.utils.io.ioDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PokieRepository @Inject constructor(
    private val client: PokieApiService,
    private val dispatcher: CoroutineDispatcher = ioDispatcher()
) {
    fun getPokemons(): Flow<ApiResult<List<Pokemon>>> {
        return flow {
            when (val pokemonList = getPokemonList()) {
                is ApiResult.Success -> {
                    val pokemonDetailsList = mutableListOf<Pokemon>()
                    pokemonList.data.forEach {
                        val pokemonDetail = getPokemonDetail(it.url)
                        if (pokemonDetail is ApiResult.Success) {
                            pokemonDetailsList.add(pokemonDetail.data)
                        }
                    }
                    emit(ApiResult.Success(pokemonDetailsList))
                }

                is ApiResult.Error -> {
                    emit(ApiResult.Error(pokemonList.message))
                }

                is ApiResult.NoInternetError -> {
                    emit(ApiResult.NoInternetError(pokemonList.message))
                }
            }
        }
    }

    private suspend fun getPokemonList(): ApiResult<List<PokemonData>> {
        return withContext(dispatcher) {
            val response = callApiClient {
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
    }

    private suspend fun getPokemonDetail(url: String): ApiResult<Pokemon> {
        return withContext(dispatcher) {
            val response = callApiClient {
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
}