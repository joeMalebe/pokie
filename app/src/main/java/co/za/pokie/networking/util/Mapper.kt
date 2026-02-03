package co.za.pokie.networking.util

import co.za.pokie.domain.model.Pokemon
import co.za.pokie.domain.model.PokemonData
import co.za.pokie.domain.model.Stat
import co.za.pokie.networking.dto.PokemonDetailsDto
import co.za.pokie.networking.dto.PokemonResponseDto

private const val MAX_BASE_SCORE = 255

fun PokemonResponseDto.mapToPokemon() = this.results.map { PokemonData(name = it.name, url = it.url) }

fun PokemonDetailsDto.mapToPokemon() =
    Pokemon(
        name = this.name,
        image = this.sprites.frontDefault,
        weight = this.weight,
        height = this.height,
        baseExperience = this.baseExperience,
        abilities = this.abilities.map { it.ability.name },
        stats = this.stats.map { Stat(name = it.stat.name, value = statProgress(it.baseStat)) },
        type = this.types.map { it.type.name },
    )

private fun statProgress(
    value: Int,
    max: Int = MAX_BASE_SCORE,
) = (value.coerceIn(0, max)).toFloat() / max.toFloat()
