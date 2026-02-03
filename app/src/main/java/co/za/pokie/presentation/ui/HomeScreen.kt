package co.za.pokie.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.za.pokie.R
import co.za.pokie.domain.model.Pokemon
import co.za.pokie.domain.viewmodel.HomeViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onPokemonClick: (String) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val viewState by viewModel.homeViewState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.loadPokemons()
    }
    val errorMessage = viewState.errorDescription

    var query by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }
    val allItems = listOf("Android", "Kotlin", "Compose", "Material", "Jetpack")

    Column(modifier) {
        when {
            viewState.isLoading -> {
                Loader()
            }

            errorMessage != null -> {
                Error(errorMessage = errorMessage)
            }

            else -> {
                PokemonContent(
                    searchQuery = viewState.searchQuery,
                    filteredList = viewState.filteredList,
                    pokemonList = viewState.pokemonList,
                    onPokemonClick = onPokemonClick,
                    onQueryChange = {
                        viewModel.filterList(it)
                    })
            }
        }

    }
}

@Composable
fun PokemonContent(
    pokemonList: List<Pokemon>,
    modifier: Modifier = Modifier,
    searchQuery: String = "",
    filteredList: List<Pokemon> = listOf(),
    onPokemonClick: (id: String) -> Unit = {},
    onQueryChange: (String) -> Unit = {}
) {
    Column(verticalArrangement = spacedBy(16.dp), modifier = Modifier.padding(top = 16.dp)) {
        OutlinedTextField(
            placeholder = {
                Text(text = stringResource(R.string.search_pokemon))
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(CenterHorizontally),
            value = searchQuery,
            onValueChange = {
                onQueryChange(it)
            })
        val items = filteredList.ifEmpty { pokemonList }
        if (searchQuery.isNotBlank() && filteredList.isEmpty()) {
            Error(
                errorHeading = stringResource(R.string.no_pokemon_found),
                errorMessage = stringResource(R.string.please_try_again)
            )
        } else
            LazyVerticalStaggeredGrid(
                modifier = modifier,
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 16.dp,
                horizontalArrangement = spacedBy(16.dp)
            ) {
                items(items) { pokemon ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable { onPokemonClick(pokemon.name) }
                                .fillMaxWidth(), horizontalAlignment = CenterHorizontally
                        ) {
                            AsyncImage(
                                contentScale = ContentScale.Crop,
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
                                Text(
                                    text = pokemon.name,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                LabelledText(
                                    stringResource(R.string.experience),
                                    pokemon.baseExperience.toString()
                                )
                            }
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
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Text(text = errorHeading, textAlign = TextAlign.Center, fontSize = 24.sp)
        Spacer(Modifier.padding(bottom = 8.dp))
        Text(text = errorMessage, textAlign = TextAlign.Center, fontSize = 18.sp)
    }
}

@Preview(showSystemUi = true)
@Composable
fun Preview() {
    MaterialTheme {
        PokemonContent(pokemonList = PreviewData.pokemonList)
    }


}