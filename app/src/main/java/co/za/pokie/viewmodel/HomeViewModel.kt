package co.za.pokie.viewmodel

import androidx.lifecycle.ViewModel
import co.za.pokie.networking.repository.PokieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pokieRepository: PokieRepository
) : ViewModel() {


}