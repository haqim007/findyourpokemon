package dev.haqim.findyourpokemon.ui.common

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import dev.haqim.findyourpokemon.domain.model.Pokemon
import dev.haqim.findyourpokemon.domain.model.dummyPokemons
import dev.haqim.findyourpokemon.ui.theme.FindYourPokemonTheme
import kotlinx.coroutines.flow.flowOf


@Composable
fun PokemonListContent(
    pagingData: LazyPagingItems<Pokemon>,
    modifier: Modifier = Modifier,
    onClickPokemon: (pokemonId: String) -> Unit,
) {
    val loadState = pagingData.loadState

    val isEmpty = loadState.refresh is LoadState.NotLoading && pagingData.itemCount == 0
    
    Column(modifier = modifier){
        if(loadState.refresh is LoadState.Loading){
            if(pagingData.itemCount < 1){
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = "Refresh Loading"
                    )

                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        else if(loadState.refresh is LoadState.Error){
            OnLoadError(loadState, pagingData)
        }else if (isEmpty){
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ){
                Text("List is empty")
            }
            
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            state = rememberLazyGridState(),
            horizontalArrangement = Arrangement.Center
        ) {

            items(
                count = pagingData.itemCount,
                key = pagingData.itemKey(),
                contentType = pagingData.itemContentType()
            ) { index ->
                val item = pagingData[index]
                item?.let {
                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {

                        PokemonItem(it, onClickPokemon )

                    }
                }
            }

        }

        if(loadState.append is LoadState.Loading){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }else if(loadState.append is LoadState.Error){
            OnLoadError(loadState, pagingData)
        }
        
    }
}

@Composable
private fun OnLoadError(
    loadState: CombinedLoadStates,
    pagingData: LazyPagingItems<Pokemon>,
) {
    val isPaginatingError = (loadState.append is LoadState.Error) ||
    pagingData.itemCount > 1
    val error = if (loadState.append is LoadState.Error)
        (loadState.append as LoadState.Error).error
    else
        (loadState.refresh as LoadState.Error).error

    val modifierError = if (isPaginatingError) {
        Modifier.padding(8.dp)
    } else {
        Modifier.fillMaxSize()
    }
    if (pagingData.itemCount == 0) {
        Column(
            modifier = modifierError,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (!isPaginatingError) {
                Icon(
                    modifier = Modifier
                        .size(64.dp),
                    imageVector = Icons.Rounded.Warning, contentDescription = null
                )
            }
            
            Log.e("pokemon list", error.stackTraceToString())

            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = error.message ?: error.toString(),
                textAlign = TextAlign.Center,
            )

            Button(
                onClick = {
                    pagingData.refresh()
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
}


@Preview(showBackground = true)
@Composable
fun PokemonListContentPreview() {
    FindYourPokemonTheme {
        PokemonListContent(
            pagingData = flowOf(PagingData.from(dummyPokemons))
                .collectAsLazyPagingItems(),
            onClickPokemon = {},
            modifier = Modifier
                .fillMaxSize()

        )
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonListEmptyContentPreview() {
    FindYourPokemonTheme {
        PokemonListContent(
            pagingData = flowOf(PagingData.from(
                listOf<Pokemon>()
            )).collectAsLazyPagingItems(),
            onClickPokemon = {},
            modifier = Modifier
                .fillMaxSize()

        )
    }
}
