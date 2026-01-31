package co.za.pokie.networking

import co.za.pokie.networking.dto.PokemonDetailsDto
import co.za.pokie.networking.dto.PokemonResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url


interface PokieApiService {

    @GET("/api/v2/pokemon")
    suspend fun getPokemonList(): Response<PokemonResponseDto>

    @GET
    suspend fun getResourceByUrl(@Url url: String?): Response<PokemonDetailsDto>
}
