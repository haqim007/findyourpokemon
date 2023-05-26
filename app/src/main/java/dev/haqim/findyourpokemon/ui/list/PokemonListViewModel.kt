package dev.haqim.findyourpokemon.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.haqim.findyourpokemon.domain.model.Pokemon
import dev.haqim.findyourpokemon.domain.repository.IPokemonRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: IPokemonRepository
): ViewModel() {
    

    val _effect: MutableSharedFlow<PokemonListUiEffect> = MutableSharedFlow(
        extraBufferCapacity = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect get() = _effect.asSharedFlow()
    private val actionStateFlow = MutableSharedFlow<PokemonListUiAction>(
        extraBufferCapacity = 10,
        replay = 2,
        onBufferOverflow = BufferOverflow.DROP_OLDEST)
    
    val pagingDataFlow: Flow<PagingData<Pokemon>>
    
    init {

        val getPokemonsAction = actionStateFlow
            .filter { 
                it is PokemonListUiAction.GetPokemons || it is PokemonListUiAction.GetOwnedPokemons
            }

        pagingDataFlow = getPokemonsAction
            .flatMapLatest { 
                if(it is PokemonListUiAction.GetPokemons){
                    onGetPokemons()
                }else{
                    onGetOwnedPokemons()
                }
            }
            .cachedIn(viewModelScope)

        actionStateFlow.updateStates().launchIn(viewModelScope)
    }

    fun processAction(action: PokemonListUiAction) {
         actionStateFlow.tryEmit(action)
    }

    private fun processEffect(effect: PokemonListUiEffect) = _effect.tryEmit(effect)

    private fun MutableSharedFlow<PokemonListUiAction>.updateStates() = onEach {
        when(it){
            is PokemonListUiAction.OpenPokemon -> 
                processEffect(PokemonListUiEffect.OpenPokemon(it.pokemonId))
            else -> {}
        }
    }
    
    private fun onGetPokemons() = repository.getPokemonList()

    private fun onGetOwnedPokemons() = repository.getOwnedPokemonList()
    
    
}

sealed class PokemonListUiAction{
    object GetPokemons: PokemonListUiAction()
    object GetOwnedPokemons: PokemonListUiAction()
    data class OpenPokemon(val pokemonId: String): PokemonListUiAction()
}

sealed class PokemonListUiEffect{
    data class OpenPokemon(val pokemonId: String): PokemonListUiEffect()
}