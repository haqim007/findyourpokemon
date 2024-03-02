package dev.haqim.findyourpokemon.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import dev.haqim.findyourpokemon.R
import dev.haqim.findyourpokemon.data.mechanism.Resource
import dev.haqim.findyourpokemon.domain.model.CatchPokemonStatus
import dev.haqim.findyourpokemon.domain.model.Pokemon
import dev.haqim.findyourpokemon.domain.model.dummyPokemons
import dev.haqim.findyourpokemon.ui.component.DialogMessage
import dev.haqim.findyourpokemon.ui.component.LoadingDialog
import dev.haqim.findyourpokemon.ui.theme.FindYourPokemonTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPokemonContent(
    modifier: Modifier = Modifier,
    pokemon: Pokemon,
    onCatchPokemon: (pokemon: Pokemon) -> Unit,
    catchingResult: Resource<CatchPokemonStatus>,
    onAddPokemonNickname: (nickname: String) -> Unit,
    onResetCatchingResult: () -> Unit,
    onReleasePokemon: (pokemonId: String) -> Unit,
) {
    val locale = Locale.current
    Box(modifier = Modifier.fillMaxSize()){
        
        when(catchingResult){
            is Resource.Loading -> 
                LoadingDialog()
            is Resource.Success -> {
                
                if(catchingResult.data == CatchPokemonStatus.SUCCESS){
                    FormNicknameDialog(onAddPokemonNickname, onResetCatchingResult)
                }else{
                    DialogMessage(
                        text = stringResource(R.string.pokemon_escaped),
                        onDismissRequest = onResetCatchingResult
                    )
                }

            }
            else -> {}
        }
        
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {

            
            AsyncImage(
                model = pokemon.avatarUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(200.dp),
                placeholder = painterResource(id = R.drawable.unknown_pokemon)
            )
            

            Text(
                text = pokemon.nickname?.let {
                    "${pokemon.name.capitalize(locale)} (${pokemon.nickname.capitalize(locale)})"
                } ?: pokemon.name.capitalize(locale),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            PokemonBadges("Types", pokemon.type, modifier)

            PokemonBadges("Moves", pokemon.move, modifier)

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { 
                    if(pokemon.isOwned){
                        onReleasePokemon(pokemon.id)
                    }else{
                        onCatchPokemon(pokemon)
                    }
                },
                colors =  ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier
                    .weight(1f, false)
                    .align(Alignment.CenterHorizontally),
            ) {
                Text(text = if(!pokemon.isOwned) {
                    stringResource(R.string.catch_the_pokemon)
                }else{
                    stringResource(R.string.release)
                })
            }

        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun FormNicknameDialog(
    onAddPokemonNickname: (nickname: String) -> Unit,
    onDismissRequest: () -> Unit
) {
    var nickname by remember { mutableStateOf("") }
    Dialog(
        onDismissRequest = onDismissRequest,
        DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    value = nickname,
                    onValueChange = { value -> nickname = value },
                    label = { Text(text = "Pokemon Nickname") },
                    placeholder = { Text(text = "Enter your pokemon nickname") },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                )
                Button(
                    onClick = { 
                        onAddPokemonNickname(nickname)
                        onDismissRequest()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier
                        .weight(1f, false)
                ) {
                    Text(stringResource(R.string.save))
                }
            }
        }
    }
}

@Composable
fun PokemonBadges(
    title: String,
    data: List<String>,
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .padding(vertical = 16.dp)
            .heightIn(max = 150.dp)
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2)
            ) {
                items(data) {
                    PokemonBadgesContent(it)
                }
            }
    }
}

@Composable
fun PokemonBadgesContent(text: String){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        elevation = CardDefaults.elevatedCardElevation(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = Color.White,
        )
    ) {
        Text(
            text = text.capitalize(Locale.current),
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Preview
@Composable
fun DetailPokemonScreenPreview() {
    FindYourPokemonTheme {
        DetailPokemonContent(
            pokemon = dummyPokemons[2],
            onCatchPokemon = {},
            catchingResult = Resource.Success(CatchPokemonStatus.FAILED),
            onAddPokemonNickname = {},
            onResetCatchingResult = {},
            onReleasePokemon = {}
        )
    }
}