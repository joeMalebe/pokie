package co.za.pokie

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import co.za.pokie.model.Pokemon1
import co.za.pokie.networking.PokieApiService
import co.za.pokie.networking.repository.PokieRepository
import co.za.pokie.networking.util.ApiResult
import co.za.pokie.ui.theme.PokieAppTheme
import co.za.pokie.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var retroFitClient: PokieApiService

    @Inject
    lateinit var repository: PokieRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            PokieAppTheme {
            }
        }
    }

    override fun onResume() {
        super.onResume()

        CoroutineScope(Dispatchers.IO).launch {
            val r = repository.getPokemons().collect {
                if (it is ApiResult.Success) {
                    Log.d("MainActivity", "onResume: $it")
                }
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun PokieApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {

                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(innerPadding)
            ) {
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector? = null,
) {
    HOME("Home"),
    FAVORITES("Favorites"),
    PROFILE("Profile"),
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    PokieAppTheme {
        Pokemon(
            pokemonList = PreviewData.pokemonList
        )
    }
}

@Composable
fun Pokemon(pokemonList: List<Pokemon1>) {
    LazyColumn {
        items(pokemonList) { pokemon ->
            Text(text = pokemon.name)
        }
    }
}

object PreviewData {
    val pokemonList = listOf(
        Pokemon1(
            name = "bulbasaur",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
            weight = 69,
            height = 7,
            baseExperience = 64
        ),
        Pokemon1(
            name = "ivysaur",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/2.png",
            weight = 130,
            height = 10,
            baseExperience = 142
        ),
        Pokemon1(
            name = "venusaur",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/3.png",
            weight = 1000,
            height = 20,
            baseExperience = 236
        ),
        Pokemon1(
            name = "charmander",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/4.png",
            weight = 85,
            height = 6,
            baseExperience = 62
        ),
        Pokemon1(
            name = "charmeleon",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/5.png",
            weight = 190,
            height = 11,
            baseExperience = 142
        ),
        Pokemon1(
            name = "charizard",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/6.png",
            weight = 905,
            height = 17,
            baseExperience = 240
        ),
        Pokemon1(
            name = "squirtle",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/7.png",
            weight = 90,
            height = 5,
            baseExperience = 63
        ),
        Pokemon1(
            name = "wartortle",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/8.png",
            weight = 225,
            height = 10,
            baseExperience = 142
        ),
        Pokemon1(
            name = "blastoise",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/9.png",
            weight = 855,
            height = 16,
            baseExperience = 239
        ),
        Pokemon1(
            name = "caterpie",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/10.png",
            weight = 29,
            height = 3,
            baseExperience = 39
        ),
        Pokemon1(
            name = "metapod",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/11.png",
            weight = 99,
            height = 7,
            baseExperience = 72
        ),
        Pokemon1(
            name = "butterfree",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/12.png",
            weight = 320,
            height = 11,
            baseExperience = 178
        ),
        Pokemon1(
            name = "weedle",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/13.png",
            weight = 32,
            height = 3,
            baseExperience = 39
        ),
        Pokemon1(
            name = "kakuna",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/14.png",
            weight = 100,
            height = 6,
            baseExperience = 72
        ),
        Pokemon1(
            name = "beedrill",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/15.png",
            weight = 295,
            height = 10,
            baseExperience = 178
        ),
        Pokemon1(
            name = "pidgey",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/16.png",
            weight = 18,
            height = 3,
            baseExperience = 50
        ),
        Pokemon1(
            name = "pidgeotto",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/17.png",
            weight = 300,
            height = 11,
            baseExperience = 122
        ),
        Pokemon1(
            name = "pidgeot",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/18.png",
            weight = 395,
            height = 15,
            baseExperience = 216
        ),
        Pokemon1(
            name = "rattata",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/19.png",
            weight = 35,
            height = 3,
            baseExperience = 51
        ),
        Pokemon1(
            name = "raticate",
            image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/20.png",
            weight = 185,
            height = 7,
            baseExperience = 145
        )
    )

}