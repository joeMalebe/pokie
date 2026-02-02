package co.za.pokie.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.za.pokie.domain.model.HomeViewData
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

    private val _homeViewData = MutableStateFlow(HomeViewData(isLoading = true))
    val homeViewData = _homeViewData.asStateFlow()

    fun loadePokemons() {
        viewModelScope.launch {
            pokieRepository.getPokemons().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _homeViewData.update {  it.copy(isLoading = false, pokemonList = result.data) }
                    }
                    is ApiResult.Error -> {
                        _homeViewData.update {  it.copy(isLoading = false, errorDescription = result.message) }
                    }
                    is ApiResult.NoInternetError -> {
                        _homeViewData.update {  it.copy(isLoading = false, errorDescription = result.message) }
                    }
                }
            }
        }
    }

}