package dev.haqim.findyourpokemon.ui.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import dev.haqim.catapp.ui.screen.navigation.Screen
import dev.haqim.findyourpokemon.ui.common.PokemonListContent


@Composable
fun PokemonListScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    ownedOnly: Boolean = false,
    viewModel: PokemonListViewModel = hiltViewModel()
){
    val pagingDataFlow = viewModel.pagingDataFlow.collectAsLazyPagingItems()
    val uiEffect by viewModel.effect.collectAsState(initial = null)
    val uiAction = {action: PokemonListUiAction -> viewModel.processAction(action)}

    // Trigger action to get pokemon
    if(!ownedOnly){
        uiAction(PokemonListUiAction.GetPokemons)
    }else{
        uiAction(PokemonListUiAction.GetOwnedPokemons)
    }

    // Perform UI effect when effectState changes
    LaunchedEffect(uiEffect) {
        uiEffect?.let { effect ->
            when (effect) {
                is PokemonListUiEffect.OpenPokemon -> {
                    navController.navigate(
                        Screen.DetailPokemon.createRoute(effect.pokemonId)
                    )
                }
            }
        }
    }
    
    PokemonListContent(
        pagingData = pagingDataFlow, 
        modifier = modifier
    ){ 
        uiAction(PokemonListUiAction.OpenPokemon(pokemonId = it))
    }

}