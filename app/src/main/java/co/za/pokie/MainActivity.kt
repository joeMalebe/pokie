package co.za.pokie

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.za.pokie.domain.model.HomeViewData
import co.za.pokie.domain.model.Pokemon1
import co.za.pokie.networking.PokieApiService
import co.za.pokie.networking.repository.PokieRepository
import co.za.pokie.networking.util.ApiResult
import co.za.pokie.ui.theme.PokieAppTheme
import co.za.pokie.domain.viewmodel.HomeViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
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
            viewModel.loadePokemons()
            val state by viewModel.homeViewData.collectAsStateWithLifecycle(this)
            PokieAppTheme {
                PokieApp(homeViewData = state)
            }
        }
    }

    override fun onResume() {
        super.onResume()

//        CoroutineScope(Dispatchers.IO).launch {
//            val r = repository.getPokemons().collect {
//                if (it is ApiResult.Success) {
//                    Log.d("MainActivity", "onResume: $it")
//                }
//            }
//        }
    }
}

@PreviewScreenSizes
@Composable
fun PokieApp(homeViewData: HomeViewData = HomeViewData()) {
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
                HomeScreen(homeViewData = homeViewData, modifier = Modifier.fillMaxWidth())
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
fun HomeScreen(modifier: Modifier = Modifier, homeViewData: HomeViewData = HomeViewData()) {
    Column(modifier) {
        when {
            homeViewData.isLoading -> {
                Loader()
            }

            homeViewData.errorDescription != null -> {
                Error(errorMessage = homeViewData.errorDescription)
            }

            else -> {
                Pokemon(pokemonList = homeViewData.pokemonList)
            }
        }
    }
}

@Composable
fun Pokemon(pokemonList: List<Pokemon1>, modifier: Modifier = Modifier) {
    LazyVerticalStaggeredGrid(modifier = modifier, columns = StaggeredGridCells.Fixed(2)) {
        items(pokemonList) { pokemon ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(), horizontalAlignment = CenterHorizontally
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .heightIn(80.dp, 120.dp)
                            .fillMaxWidth(0.5f),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(pokemon.image)
                            .placeholder(R.drawable.ic_launcher_foreground) // Use a local drawable as placeholder
                            .build(),
                        contentDescription = pokemon.name
                    )
                    Column(
                        modifier = Modifier, verticalArrangement = spacedBy(8.dp)
                    ) {
                        Text(text = pokemon.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        LabelledText(stringResource(R.string.experience), pokemon.baseExperience.toString())
                    }
                }
            }
        }
    }
}

@Composable
fun LabelledText(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(0.5f)) {
        Text(text = label, fontSize = 14.sp)
        Text(text = value, fontSize = 16.sp)
    }
}

@Composable
@Preview(showSystemUi = true)
fun Loader(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Center) {
        CircularProgressIndicator()
    }
}

@Composable
@Preview(showSystemUi = true)
fun Error(
    modifier: Modifier = Modifier,
    errorHeading: String = stringResource(R.string.error_occurred),
    errorMessage: String = stringResource(
        R.string.please_try_again_later
    )
) {
        Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = CenterHorizontally) {
            Text(text = errorHeading, textAlign = TextAlign.Center  ,fontSize = 24.sp)
            Spacer(Modifier.padding(bottom = 8.dp))
            Text(text = errorMessage, textAlign = TextAlign.Center  ,fontSize = 18.sp)
        }
}

object PreviewData {
    val homeViewData = HomeViewData()
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