package co.za.pokie.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.za.pokie.domain.model.HomeViewState
import co.za.pokie.domain.model.PageData
import co.za.pokie.domain.model.Pokemon
import co.za.pokie.domain.model.PokieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val pokieRepository: PokieRepository,
) : ViewModel() {
    private var pageNumber = 1
    private var isPaginationComplete = false
    private val _homeViewData = MutableStateFlow(HomeViewState(isLoading = true))
    val homeViewState = _homeViewData.asStateFlow()

    fun loadMorePokemonPage(coroutineContext: CoroutineContext = Dispatchers.IO) {
        if (isPaginationComplete) return
        _homeViewData.update { it.copy(isLoading = false, isPageLoading = true) }
        viewModelScope.launch(coroutineContext) {
            loadPageData()
        }
    }

    fun loadInitialPokemonsPage(coroutineContext: CoroutineContext = Dispatchers.IO) {
        if(_homeViewData.value.isDataLoaded) return
        viewModelScope.launch(coroutineContext) {
            loadPageData()
        }
    }

    private suspend fun loadPageData() {
            pokieRepository.getPokemons1(pageNumber).collect { result ->
                result.onSuccess { pageData ->
                    handlePageLoadSuccessResult(pageData)
                }.onFailure { result ->
                    handlePageLoadFailureResult(result)
                }
            }
    }

    private fun handlePageLoadFailureResult(result: Throwable) {
        _homeViewData.update {
            it.copy(
                isDataLoaded = true,
                isPageLoading = false,
                isLoading = false,
                searchQuery = "",
                errorDescription = result.message,
            )
        }
    }

    private fun handlePageLoadSuccessResult(pageData: PageData) {
        _homeViewData.update { currentState ->
            pageNumber = pageData.currentPage
            isPaginationComplete = pageData.isLastPage
            HomeViewState(
                isDataLoaded = true,
                pokemonList = (currentState.pokemonList + pageData.pokemons).distinctBy { pokemon -> pokemon.name },
            )
        }
    }

    fun getPokemonDetails(name: String): Pokemon? = this._homeViewData.value.pokemonList.firstOrNull { it.name == name }

    fun filterList(search: String) {
        _homeViewData.update {
            it.copy(
                searchQuery = search,
                filteredList =
                it.pokemonList.filter { pokemon ->
                    pokemon.name.contains(
                        search,
                        ignoreCase = true,
                    )
                },
            )
        }
    }
}
