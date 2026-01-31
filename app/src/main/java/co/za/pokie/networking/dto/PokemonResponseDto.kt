package co.za.pokie.networking.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class PokemonResponseDto(
    @SerialName("count")
    val count: Int = 0,
    @SerialName("next")
    val next: String = "",
    @SerialName("previous")
    val previous: String? = null,
    @SerialName("results")
    val results: List<ResultDto> = listOf()
)

@Serializable
data class ResultDto(
    @SerialName("name")
    val name: String = "",
    @SerialName("url")
    val url: String = ""
)


