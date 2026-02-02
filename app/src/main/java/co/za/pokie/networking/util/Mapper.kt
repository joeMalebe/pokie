package co.za.pokie.networking.util

import co.za.pokie.domain.model.Pokemon
import co.za.pokie.domain.model.Pokemon1
import co.za.pokie.networking.dto.PokemonDetailsDto
import co.za.pokie.networking.dto.PokemonResponseDto

fun PokemonResponseDto.mapToPokemon() =
    this.results.map { Pokemon(name = it.name, url = it.url) }

fun PokemonDetailsDto.mapToPokemon() = Pokemon1(
    name = this.name,
    image = this.sprites.frontDefault,
    weight = this.weight,
    height = this.height,
    baseExperience = this.baseExperience
)