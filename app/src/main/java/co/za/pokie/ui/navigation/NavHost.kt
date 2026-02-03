package co.za.pokie.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import co.za.pokie.domain.viewmodel.HomeViewModel
import co.za.pokie.ui.components.DetailsScreen
import co.za.pokie.ui.components.HomeScreen

@Composable
fun PokieNavHost() {

    val backStack = rememberNavBackStack(Route.Home)
    val viewModel = hiltViewModel<HomeViewModel>()

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is Route.Home -> {
                    NavEntry(key) {
                        HomeScreen(viewModel = viewModel, onPokemonClick = {
                            viewModel.selectedPokemonName = it
                            backStack.add(Route.Details(it))
                        })
                    }
                }

                is Route.Details -> {
                    NavEntry(key) {
                        DetailsScreen(key.name, viewModel)
                    }
                }

                else -> error("Unknown key: $key")
            }
        }
    )
}


