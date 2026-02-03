package co.za.pokie.domain.viewmodel

import co.za.pokie.domain.model.HomeViewState
import co.za.pokie.networking.repository.PokieRepository
import co.za.pokie.networking.util.ApiResult
import co.za.pokie.presentation.ui.PreviewData
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
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
    fun `loadPokemons should return pokemon data when ApiResult is successful`() =
        runTest {
            val testDispatcher = Dispatchers.Unconfined
            whenever(mockRepository.getPokemons()).thenReturn(flowOf(ApiResult.Success(PreviewData.pokemonList)))
            val results = mutableListOf<HomeViewState>()
            backgroundScope.launch(testDispatcher) {
                viewModel.homeViewState.toList(results)
            }

            viewModel.loadPokemons(testDispatcher)

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
    fun `loadPokemons should return error state when ApiResult is error`() =
        runTest {
            val testDispatcher = Dispatchers.Unconfined
            whenever(mockRepository.getPokemons()).thenReturn(flowOf(ApiResult.Error("Error")))
            val results = mutableListOf<HomeViewState>()
            backgroundScope.launch(testDispatcher) {
                viewModel.homeViewState.toList(results)
            }

            viewModel.loadPokemons(testDispatcher)

            assertEquals(2, results.size)
            assertNotNull(results)
            assertTrue(results.first().isLoading)
            assertFalse(results.first().isDataLoaded)
            assertTrue(results[1].isDataLoaded)
            assertFalse(results[1].isLoading)
            assertTrue(results[1].pokemonList.isEmpty())
            assertEquals("Error", results[1].errorDescription)
        }
}
