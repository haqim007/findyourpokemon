package dev.haqim.findyourpokemon.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.haqim.findyourpokemon.data.mechanism.Resource
import dev.haqim.findyourpokemon.domain.model.CatchPokemonStatus
import dev.haqim.findyourpokemon.domain.model.Pokemon
import dev.haqim.findyourpokemon.domain.repository.IPokemonRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailPokemonViewModel @Inject constructor(
    private val repository: IPokemonRepository
): ViewModel(){

    private val _state = MutableStateFlow(DetailPokemonUiState())
    val state = _state.stateIn(
        viewModelScope, SharingStarted.Eagerly, DetailPokemonUiState()
    )

    val _effect: MutableSharedFlow<DetailPokemonUiEffect> = MutableSharedFlow(
        extraBufferCapacity = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect get() = _effect.asSharedFlow()
    private val actionStateFlow = MutableSharedFlow<DetailPokemonUiAction>(
        extraBufferCapacity = 10,
        replay = 2,
        onBufferOverflow = BufferOverflow.DROP_OLDEST)

    init {
        actionStateFlow.updateStates().launchIn(viewModelScope)
    }

    fun processAction(action: DetailPokemonUiAction) {
        actionStateFlow.tryEmit(action)
    }

    private fun processEffect(effect: DetailPokemonUiEffect) = _effect.tryEmit(effect)

    private fun MutableSharedFlow<DetailPokemonUiAction>.updateStates() = onEach {
        when(it){
            is DetailPokemonUiAction.GetDetailPokemon -> {
                viewModelScope.launch { 
                    repository.getPokemon(it.pokemonId)
                        .collect{
                            _state.update { state ->
                                state.copy(
                                    pokemon = it
                                )
                            }
                        }
                }
            }
            is DetailPokemonUiAction.NavigateBack -> 
                processEffect(DetailPokemonUiEffect.NavigateBack)
            is DetailPokemonUiAction.CatchPokemon -> {
                viewModelScope.launch {
                    repository.catchPokemon(it.pokemonId).collect{
                        _state.update { state ->
                            state.copy(
                                catchingResult = it
                            )
                        }
                    }
                }
            }
            is DetailPokemonUiAction.AddPokemonNickname -> {
                repository.addPokemonNickname(it.pokemonId, it.nickname)
            }
            is DetailPokemonUiAction.ResetCatchingResult -> {
                // reset catchingResult state
                _state.update { state ->
                    state.copy(
                        catchingResult = Resource.Idle()
                    )
                }
            }
            is DetailPokemonUiAction.ReleasePokemon->{
               repository.releasePokemon(it.pokemonId) 
            }
                
        }
    }
    
}

data class DetailPokemonUiState(
    val pokemon: Resource<Pokemon?> = Resource.Idle(),
    val catchingResult: Resource<CatchPokemonStatus> = Resource.Idle(),
)

sealed class DetailPokemonUiAction{
    data class GetDetailPokemon(val pokemonId: String): DetailPokemonUiAction()
    data class CatchPokemon(val pokemonId: String): DetailPokemonUiAction()
    data class ReleasePokemon(val pokemonId: String): DetailPokemonUiAction()
    object ResetCatchingResult: DetailPokemonUiAction()
    object NavigateBack: DetailPokemonUiAction()
    data class AddPokemonNickname(val pokemonId: String, val nickname: String): DetailPokemonUiAction()
}

sealed class DetailPokemonUiEffect{
    object NavigateBack: DetailPokemonUiEffect()
}