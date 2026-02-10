package co.za.pokie.domain.model

import kotlinx.coroutines.flow.Flow

interface PokieRepository {
    fun getPokemons(): Flow<Result<List<Pokemon>>>
}
