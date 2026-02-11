package co.za.pokie.domain.model

import kotlinx.coroutines.flow.Flow

interface PokieRepository {
    fun getPokemons(): Flow<Result<List<Pokemon>>>
    //fun getPokemons(pageNumber:Int = 0, pageSize: Int = 10): Flow<Result<List<Pokemon>>>
    fun getPokemons1(pageNumber:Int = 1, pageSize: Int = 10): Flow<Result<PageData>>
}
