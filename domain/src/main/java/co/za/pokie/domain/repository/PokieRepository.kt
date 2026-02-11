package co.za.pokie.domain.repository

import co.za.pokie.domain.model.PageData
import kotlinx.coroutines.flow.Flow

interface PokieRepository {
    fun getPokemons(pageNumber: Int = 1, pageSize: Int = 10): Flow<Result<PageData>>
}
