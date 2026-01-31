package co.za.pokie.networking.util

import co.za.pokie.model.Pokemon
import co.za.pokie.model.Pokemon1
import co.za.pokie.networking.dto.PokemonDetailsDto
import co.za.pokie.networking.dto.PokemonResponseDto

const val DEFAULT_UNKNOWN_RESPONSE_FIELD = "N/A"

fun PokemonResponseDto.mapToPokemon() =
    this.results.map { Pokemon(name = it.name, url = it.url) }

fun PokemonDetailsDto.mapToPokemon() = Pokemon1(
        name = this.name,
        image = this.sprites.frontDefault,
        weight = this.weight,
        height = this.height,
        baseExperience = this.baseExperience
    )