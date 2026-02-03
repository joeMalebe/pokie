package co.za.pokie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import co.za.pokie.networking.PokieApiService
import co.za.pokie.networking.repository.PokieRepository
import co.za.pokie.ui.components.PokieApp
import co.za.pokie.ui.theme.PokieAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var retroFitClient: PokieApiService

    @Inject
    lateinit var repository: PokieRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            PokieAppTheme {
                PokieApp()
            }
        }
    }
}