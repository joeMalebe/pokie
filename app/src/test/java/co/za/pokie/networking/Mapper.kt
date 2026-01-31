package co.za.pokie.networking

import co.za.pokie.networking.dto.PokemonDetailsDto
import co.za.pokie.networking.dto.PokemonResponseDto
import co.za.pokie.networking.util.mapToPokemon
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MapperTest {
    val json by lazy {
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }

    val pokemonDetailsDto by lazy {
        json.decodeFromString<PokemonDetailsDto>(loadJson("pokemonDetail.json"))
    }

    @Test
    fun `mapToPokemon should map fields correctly`() {
        val result = pokemonDetailsDto.mapToPokemon()

        assertEquals(3, result.height)
        assertEquals(29, result.weight)
        assertEquals(39, result.baseExperience)
        assertEquals("caterpie", result.name)
        assertEquals("caterpie", result.image)
    }

    @Test
    fun `toDay should return correct day`() {
        val s = loadJson("pokemonList.json")
        val c = loadJson("pokemonDetail.json")

        val a = json.decodeFromString<PokemonResponseDto>(s)
        val b = json.decodeFromString<PokemonDetailsDto>(c)
        assertNull(b)
        assertNull(a)
    }

    private fun loadJson(name: String): String {
        return this::class.java.classLoader!!
            .getResource(name)!!
            .readText()
    }
}