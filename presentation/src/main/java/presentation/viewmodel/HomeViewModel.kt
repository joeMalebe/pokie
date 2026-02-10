package co.za.pokie.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.za.pokie.domain.model.HomeViewState
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
        private val _homeViewData = MutableStateFlow(HomeViewState(isLoading = true))
        val homeViewState = _homeViewData.asStateFlow()

        fun loadPokemons(coroutineContext: CoroutineContext = Dispatchers.IO) {
            viewModelScope.launch(coroutineContext) {
                if (_homeViewData.value.isDataLoaded) return@launch
                pokieRepository.getPokemons().collect { result ->
                    result.onSuccess { pokemons ->
                        _homeViewData.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                isDataLoaded = true,
                                pokemonList = (currentState.pokemonList + pokemons).distinctBy { pokemon -> pokemon.name },
                            )
                        }
                    }.onFailure {
                            _homeViewData.update { currentState ->
                                currentState.copy(
                                    isLoading = false,
                                    isDataLoaded = true,
                                    errorDescription = it.message,
                                )
                            }

                        }
                    }
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
