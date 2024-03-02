package dev.haqim.findyourpokemon.ui.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.haqim.findyourpokemon.R
import dev.haqim.findyourpokemon.data.mechanism.Resource
import dev.haqim.findyourpokemon.ui.component.PlainMessage

@Composable
fun DetailPokemonScreen(
    id: String,
    modifier: Modifier = Modifier,
    viewModel: DetailPokemonViewModel = hiltViewModel(),
    navigateBack: () -> Unit
){

    val uiAction = {action: DetailPokemonUiAction -> viewModel.processAction(action)}
    val uiState by viewModel.state.collectAsState()
    val uiEffect by viewModel.effect.collectAsState(initial = null)

    // Perform UI effect when effectState changes
    LaunchedEffect(uiEffect) {
        uiEffect?.let { effect ->
            when (effect) {
                is DetailPokemonUiEffect.NavigateBack -> {
                    navigateBack()
                }

                else -> {}
            }
        }
    }
    val pokemon = uiState.pokemon
    val catchingResult = uiState.catchingResult
    
    // get detail pokemon
    LaunchedEffect(key1 = Unit){
        uiAction(DetailPokemonUiAction.GetDetailPokemon(id))
    }
    
    Column(modifier = modifier.fillMaxSize()) {
        Box {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.back_to_prev_screen),
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { uiAction(DetailPokemonUiAction.NavigateBack) }
            )
        }
        
        when(pokemon){
            is Resource.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
            is Resource.Error ->  {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = pokemon.message ?: "error occured",
                        textAlign = TextAlign.Center,
                    )

                    Button(
                        onClick = {
                            uiAction(DetailPokemonUiAction.GetDetailPokemon(id))
                        },
                        content = {
                            Text(text = "Refresh")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White,
                        )
                    )
                }
            }
            is Resource.Success -> {
                pokemon.data?.let { data ->
                    DetailPokemonContent(
                        pokemon = data, 
                        catchingResult = catchingResult,
                        onCatchPokemon = {
                            uiAction(DetailPokemonUiAction.CatchPokemon(id))
                        },
                        onAddPokemonNickname = {nickname ->
                            uiAction(DetailPokemonUiAction.AddPokemonNickname(id, nickname))
                        },
                        onResetCatchingResult = {
                            uiAction(DetailPokemonUiAction.ResetCatchingResult)
                        },
                        onReleasePokemon = {pokemonId -> 
                            uiAction(DetailPokemonUiAction.ReleasePokemon(pokemonId))
                        }
                    )
                } ?: PlainMessage(message = stringResource(R.string.no_data_found))
            }
            else -> {}
        }
    }
    
}
