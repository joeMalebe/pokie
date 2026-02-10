package co.za.pokie.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import co.za.pokie.presentation.R
import co.za.pokie.domain.model.Pokemon
import co.za.pokie.presentation.theme.PokieAppTheme
import co.za.pokie.presentation.viewmodel.HomeViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun DetailsScreen(
    name: String,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    viewModel.getPokemonDetails(name = name)?.let { DetailsContent(pokemon = it) }
}

@Composable
fun DetailsContent(
    modifier: Modifier = Modifier,
    pokemon: Pokemon = PreviewData.pokemonList.first(),
) {
    LazyColumn(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        item {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = CenterHorizontally) {
                AsyncImage(
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.heightIn(320.dp, 640.dp),
                    model =
                        ImageRequest.Builder(LocalContext.current)
                            .placeholder(R.drawable.ic_launcher_foreground).data(pokemon.image)
                            .build(),
                    contentDescription = pokemon.name,
                )
            }
        }

        item {
            Column(Modifier.fillMaxWidth(), verticalArrangement = spacedBy(16.dp)) {
                Bio(pokemon)
                HorizontalDivider()
                BasicInfo(pokemon)
                Abilities(pokemon)
                Stats(pokemon, Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun Bio(
    pokemon: Pokemon,
    modifier: Modifier = Modifier,
) {
    Column(modifier, verticalArrangement = spacedBy(8.dp)) {
        Text("Hello ${pokemon.name}", fontSize = 24.sp)
        Row(horizontalArrangement = spacedBy(8.dp)) {
            pokemon.type.forEach {
                PillTag(it, MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
private fun BasicInfo(
    pokemon: Pokemon,
    modifier: Modifier = Modifier,
) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = spacedBy(8.dp)) {
        Vitals(
            label = stringResource(R.string.weight),
            value = "${pokemon.weight}kg",
            modifier = Modifier.weight(0.2f, false),
        )
        Vitals(
            label = stringResource(R.string.height),
            value = "${pokemon.height}m",
            modifier = Modifier.weight(0.2f, false),
        )
    }
}

@Composable
private fun Stats(
    pokemon: Pokemon,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxWidth(), verticalArrangement = spacedBy(8.dp)) {
        pokemon.stats.forEach {
            Text(text = it.name)
            LinearProgressIndicator(
                trackColor = MaterialTheme.colorScheme.onSecondary,
                color = MaterialTheme.colorScheme.secondary,
                strokeCap = StrokeCap.Square,
                progress = { it.value },
                modifier =
                    modifier
                        .fillMaxWidth()
                        .height(12.dp),
            )
        }
    }
}

@Composable
private fun Abilities(
    pokemon: Pokemon,
    modifier: Modifier = Modifier,
) {
    Column(modifier, verticalArrangement = spacedBy(8.dp)) {
        Text(stringResource(R.string.abilities), fontSize = 14.sp)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = spacedBy(8.dp)) {
            pokemon.abilities.forEach {
                Vitals(value = it, Modifier.weight(0.3f))
            }
        }
    }
}

@Composable
fun PillTag(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier =
            modifier
                .clip(RoundedCornerShape(50))
                .background(color)
                .padding(horizontal = 12.dp, vertical = 6.dp),
    )
}

@Composable
private fun Vitals(
    value: String,
    modifier: Modifier = Modifier,
    label: String? = null,
) {
    Column(modifier.fillMaxWidth(), verticalArrangement = spacedBy(8.dp)) {
        label?.let { Text(it, fontSize = 14.sp) }
        Box(
            contentAlignment = Center,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
        ) {
            Text(value, fontSize = 18.sp, maxLines = 1, overflow = Ellipsis)
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun DetailsContentPreview() {
    PokieAppTheme {
        DetailsContent(pokemon = PreviewData.pokemonList.first())
    }
}
