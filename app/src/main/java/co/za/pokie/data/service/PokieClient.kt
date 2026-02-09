package co.za.pokie.data.service

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class PokieClient(okHttpClient: OkHttpClient) {
    val json =
        Json {
            ignoreUnknownKeys
        }
    val client =
        Retrofit
            .Builder()
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(" https://pokeapi.co/").build()
}
