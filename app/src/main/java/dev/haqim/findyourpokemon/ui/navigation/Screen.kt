package dev.haqim.catapp.ui.screen.navigation

sealed class Screen(val route: String) {
    object PokemonList : Screen("pokemonList")
    object OwnedPokemonList : Screen("ownedPokemonList")
    object DetailPokemon: Screen("pokemonList/{id}"){
        fun createRoute(id: String) = "pokemonList/$id"
    }
}