package co.za.pokie.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object Home : Route, NavKey

    @Serializable
    data class Details(
        val name: String = "N/A",
    ) : Route,
        NavKey
}
