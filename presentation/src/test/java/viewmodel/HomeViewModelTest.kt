package viewmodel

import co.za.pokie.domain.model.HomeViewState
import co.za.pokie.domain.model.PageData
import co.za.pokie.domain.model.Pokemon
import co.za.pokie.domain.model.PokieRepository
import co.za.pokie.domain.model.Stat
import co.za.pokie.presentation.ui.PreviewData
import co.za.pokie.presentation.viewmodel.HomeViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.fail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.atMost
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class HomeViewModelTest {
    @Mock
    lateinit var mockRepository: PokieRepository
    lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        mockRepository = mock<PokieRepository>()
        viewModel = HomeViewModel(mockRepository)
    }

    @Test
    fun `loadInitialPokemonsPage should return pokemon data when ApiResult is successful`() = runTest {
        val testDispatcher = Dispatchers.Unconfined
        whenever(mockRepository.getPokemons(1)).thenReturn(
            flowOf(
                Result.success(
                    PageData(
                        currentPage = 1,
                        isLastPage = false,
                        pokemons = PreviewData.pokemonList,
                    ),
                ),
            ),
        )
        val results = mutableListOf<HomeViewState>()
        backgroundScope.launch(testDispatcher) {
            viewModel.homeViewState.toList(results)
        }

        viewModel.loadInitialPokemonsPage(testDispatcher)

        assertEquals(2, results.size)
        assertNotNull(results)
        assertTrue(results.first().isLoading)
        assertFalse(results.first().isDataLoaded)
        assertTrue(results[1].isDataLoaded)
        assertFalse(results[1].isLoading)
        assertFalse(results[1].pokemonList.isEmpty())
        assertNull(results[1].errorDescription)
        assertNull(results[1].errorHeading)
    }

    @Test
    fun `loadInitialPokemons should only be called once`() = runTest {
        val testDispatcher = Dispatchers.Unconfined
        whenever(mockRepository.getPokemons(1)).thenReturn(
            flowOf(
                Result.success(
                    PageData(
                        currentPage = 1,
                        isLastPage = false,
                        pokemons = PreviewData.pokemonList,
                    ),
                ),
            ),
        )
        val results = mutableListOf<HomeViewState>()
        backgroundScope.launch(testDispatcher) {
            viewModel.homeViewState.toList(results)
        }

        viewModel.loadInitialPokemonsPage(testDispatcher)
        viewModel.loadInitialPokemonsPage(testDispatcher)

        verify(mockRepository, atMost(1)).getPokemons(any(), any())
    }

    @Test
    fun `loadMorePokemonPage should return pokemon data when ApiResult is successful`() = runTest {
        val testDispatcher = Dispatchers.Unconfined
        whenever(mockRepository.getPokemons(1)).thenReturn(
            flowOf(
                Result.success(
                    PageData(
                        currentPage = 1,
                        isLastPage = false,
                        pokemons = PreviewData.pokemonList,
                    ),
                ),
            ),
        )
        val results = mutableListOf<HomeViewState>()
        backgroundScope.launch(testDispatcher) {
            viewModel.homeViewState.toList(results)
        }

        viewModel.loadMorePokemonPage(testDispatcher)

        assertEquals(3, results.size)
        assertNotNull(results)
        assertTrue(results.first().isLoading)
        assertFalse(results.first().isDataLoaded)
        assertTrue(results[2].isDataLoaded)
        assertTrue(results[1].isPageLoading)
        assertFalse(results[1].isLoading)
        assertFalse(results[2].pokemonList.isEmpty())
        assertNull(results[2].errorDescription)
        assertNull(results[2].errorHeading)
    }

    @Test
    fun `loadMorePokemonPage should not run when current page is the last page`() = runTest {
        val testDispatcher = Dispatchers.Unconfined
        whenever(mockRepository.getPokemons(1)).thenReturn(
            flowOf(
                Result.success(
                    PageData(
                        currentPage = 1,
                        isLastPage = true,
                        pokemons = PreviewData.pokemonList,
                    ),
                ),
            ),
        )
        val results = mutableListOf<HomeViewState>()
        backgroundScope.launch(testDispatcher) {
            viewModel.homeViewState.toList(results)
        }

        viewModel.loadMorePokemonPage(testDispatcher)
        viewModel.loadMorePokemonPage(testDispatcher)

        verify(mockRepository, atMost(1)).getPokemons(any(), any())
    }

    @Test
    fun `loadPokemons should return error state when ApiResult is error`() = runTest {
        val testDispatcher = Dispatchers.Unconfined
        whenever(mockRepository.getPokemons()).thenReturn(flowOf(Result.failure(RuntimeException("Error"))))
        val results = mutableListOf<HomeViewState>()
        backgroundScope.launch(testDispatcher) {
            viewModel.homeViewState.toList(results)
        }

        viewModel.loadInitialPokemonsPage(testDispatcher)

        assertEquals(2, results.size)
        assertNotNull(results)
        assertTrue(results.first().isLoading)
        assertFalse(results.first().isDataLoaded)
        assertTrue(results[1].isDataLoaded)
        assertFalse(results[1].isLoading)
        assertTrue(results[1].pokemonList.isEmpty())
        assertEquals("Error", results[1].errorDescription)
    }

    @Test
    fun `given pokemons list when getPokemon page load fails then emit page error`() = runTest {
        val testDispatcher = Dispatchers.Unconfined
        whenever(mockRepository.getPokemons(1)).thenReturn(
            flowOf(
                Result.success(
                    PageData(
                        currentPage = 2,
                        isLastPage = false,
                        pokemons = PreviewData.pokemonList,
                    ),
                ),
            ),
        )
        whenever(mockRepository.getPokemons(2)).thenReturn(
            flowOf(
                Result.failure(
                    RuntimeException("Error"),
                ),
            ),
        )

        val results = mutableListOf<HomeViewState>()
        backgroundScope.launch(testDispatcher) {
            viewModel.homeViewState.toList(results)
        }
        viewModel.loadInitialPokemonsPage(testDispatcher)
        viewModel.loadMorePokemonPage(testDispatcher)

        assertEquals("Error", results[3].pageLoadingErrorDescription)
    }

    @Test
    fun `given pokemons list when filterList then emit filtered pokemons results`() = runTest {
        val testDispatcher = Dispatchers.Unconfined
        whenever(mockRepository.getPokemons(1)).thenReturn(
            flowOf(
                Result.success(
                    PageData(
                        currentPage = 1,
                        isLastPage = false,
                        pokemons = PreviewData.pokemonList,
                    ),
                ),
            ),
        )
        val results = mutableListOf<HomeViewState>()
        backgroundScope.launch(testDispatcher) {
            viewModel.homeViewState.toList(results)
        }
        viewModel.loadInitialPokemonsPage(testDispatcher)

        viewModel.filterList("cha")
        assertEquals(3, viewModel.homeViewState.value.filteredList.size)
        assertEquals(20, viewModel.homeViewState.value.pokemonList.size)
    }

    @Test
    fun `given pokemons list when getPokemonsDetail then return pokemon details`() = runTest {
        val testDispatcher = Dispatchers.Unconfined
        whenever(mockRepository.getPokemons(1)).thenReturn(
            flowOf(
                Result.success(
                    PageData(
                        currentPage = 1,
                        isLastPage = false,
                        pokemons = PreviewData.pokemonList,
                    ),
                ),
            ),
        )
        val results = mutableListOf<HomeViewState>()
        backgroundScope.launch(testDispatcher) {
            viewModel.homeViewState.toList(results)
        }
        viewModel.loadInitialPokemonsPage(testDispatcher)

        viewModel.getPokemonDetails("charmeleon")?.let { result ->

            assertEquals("charmeleon", result.name)
            assertEquals(11, result.height)
        } ?: fail("no Pokemon with name in list")
    }
}

object TestData {
    val pokemonList =
        listOf(
            Pokemon(
                name = "bulbasaur",
                image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
                weight = 69,
                height = 7,
                baseExperience = 64,
                abilities =
                listOf(
                    "tall",
                    "fast",
                    "small",
                ),
                stats =
                listOf(
                    Stat(name = "hp", value = 0.28f),
                    Stat(name = "attack", value = 0.50f),
                    Stat(
                        name = "defense",
                        value = 0.84f,
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
            ),
        )
}
