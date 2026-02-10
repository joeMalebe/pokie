package co.za.pokie.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import co.za.pokie.presentation.ui.DetailsScreen
import co.za.pokie.presentation.ui.HomeScreen
import co.za.pokie.presentation.viewmodel.HomeViewModel

@Composable
fun PokieNavHost() {
    val backStack = rememberNavBackStack(Route.Home)
    val viewModel = hiltViewModel<HomeViewModel>()

    NavDisplay(
        backStack = backStack,
        entryDecorators =
            listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is Route.Home -> {
                    NavEntry(key) {
                        HomeScreen(viewModel = viewModel, onPokemonClick = {
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
        },
    )
}
