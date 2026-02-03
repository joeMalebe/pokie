package co.za.pokie.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.za.pokie.domain.model.HomeViewState
import co.za.pokie.domain.model.Pokemon1
import co.za.pokie.networking.repository.PokieRepository
import co.za.pokie.networking.util.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pokieRepository: PokieRepository
) : ViewModel() {

    private val _homeViewData = MutableStateFlow(HomeViewState(isLoading = true))
    val homeViewState = _homeViewData.asStateFlow()

    var selectedPokemonName = ""

    fun loadPokemons() {
        viewModelScope.launch {
            if(_homeViewData.value.isDataLoaded) return@launch
            pokieRepository.getPokemons().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _homeViewData.update {  it.copy(isLoading = false, isDataLoaded = true, pokemonList = result.data) }
                    }
                    is ApiResult.Error -> {
                        _homeViewData.update {  it.copy(isLoading = false, isDataLoaded = true, errorDescription = result.message) }
                    }
                    is ApiResult.NoInternetError -> {
                        _homeViewData.update {  it.copy(isLoading = false, isDataLoaded = true, errorDescription = result.message) }
                    }
                }
            }
        }
    }

    fun getPokemonDetails(name: String): Pokemon1? =
        this._homeViewData.value.pokemonList.firstOrNull { it.name == name }


}