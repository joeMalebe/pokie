package co.za.pokie.presentation.ui

import co.za.pokie.domain.model.Pokemon
import co.za.pokie.domain.model.Stat

object PreviewData {
    val pokemonList = listOf(
        Pokemon(
            name = "bulbasaur",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
            weight = 69,
            height = 7,
            baseExperience = 64,
            abilities = listOf(
                "tall",
                "fast",
                "small"
            ),
            stats = listOf(
                Stat(name = "hp", value = 0.28f), Stat(name = "attack", value = 0.50f),
                Stat(
                    name = "defense",
                    value = 0.84f
                ),
            ),
            type = listOf("grass", "poison"),
        ),
        Pokemon(
            name = "ivysaur",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/2.png",
            weight = 130,
            height = 10,
            baseExperience = 142,
        ),
        Pokemon(
            name = "venusaur",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/3.png",
            weight = 1000,
            height = 20,
            baseExperience = 236,
        ),
        Pokemon(
            name = "charmander",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/4.png",
            weight = 85,
            height = 6,
            baseExperience = 62,
        ),
        Pokemon(
            name = "charmeleon",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/5.png",
            weight = 190,
            height = 11,
            baseExperience = 142,
        ),
        Pokemon(
            name = "charizard",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/6.png",
            weight = 905,
            height = 17,
            baseExperience = 240,
        ),
        Pokemon(
            name = "squirtle",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/7.png",
            weight = 90,
            height = 5,
            baseExperience = 63,
        ),
        Pokemon(
            name = "wartortle",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/8.png",
            weight = 225,
            height = 10,
            baseExperience = 142,
        ),
        Pokemon(
            name = "blastoise",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/9.png",
            weight = 855,
            height = 16,
            baseExperience = 239,
        ),
        Pokemon(
            name = "caterpie",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/10.png",
            weight = 29,
            height = 3,
            baseExperience = 39,
        ),
        Pokemon(
            name = "metapod",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/11.png",
            weight = 99,
            height = 7,
            baseExperience = 72,
        ),
        Pokemon(
            name = "butterfree",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/12.png",
            weight = 320,
            height = 11,
            baseExperience = 178,
        ),
        Pokemon(
            name = "weedle",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/13.png",
            weight = 32,
            height = 3,
            baseExperience = 39,
        ),
        Pokemon(
            name = "kakuna",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/14.png",
            weight = 100,
            height = 6,
            baseExperience = 72,
        ),
        Pokemon(
            name = "beedrill",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/15.png",
            weight = 295,
            height = 10,
            baseExperience = 178,
        ),
        Pokemon(
            name = "pidgey",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/16.png",
            weight = 18,
            height = 3,
            baseExperience = 50,
        ),
        Pokemon(
            name = "pidgeotto",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/17.png",
            weight = 300,
            height = 11,
            baseExperience = 122,
        ),
        Pokemon(
            name = "pidgeot",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/18.png",
            weight = 395,
            height = 15,
            baseExperience = 216,
        ),
        Pokemon(
            name = "rattata",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/19.png",
            weight = 35,
            height = 3,
            baseExperience = 51,
        ),
        Pokemon(
            name = "raticate",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/20.png",
            weight = 185,
            height = 7,
            baseExperience = 145,
        )
    )
}