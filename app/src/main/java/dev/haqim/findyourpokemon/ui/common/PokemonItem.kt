package dev.haqim.findyourpokemon.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dev.haqim.findyourpokemon.R
import dev.haqim.findyourpokemon.domain.model.Pokemon
import dev.haqim.findyourpokemon.domain.model.dummyPokemons
import dev.haqim.findyourpokemon.ui.theme.FindYourPokemonTheme


@Composable
fun PokemonItem(pokemon: Pokemon, onClickPokemon: (id: String) -> Unit, modifier: Modifier = Modifier) {
    val locale = Locale.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable { onClickPokemon(pokemon.id) },
        elevation = CardDefaults.elevatedCardElevation(),
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .size(250.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = pokemon.avatarUrl,
                contentDescription = stringResource(R.string.pokemon_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp),
                error = painterResource(id = R.drawable.unknown_pokemon),
                placeholder = painterResource(id = R.drawable.unknown_pokemon)
            )

            Text(
                text = pokemon.nickname?.let {
                    "${pokemon.name.capitalize(locale)} (${pokemon.nickname.capitalize(locale)})"
                } ?: pokemon.name.capitalize(locale),
                overflow = TextOverflow.Ellipsis,
                maxLines = 3,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.height(50.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonItemPreview() {
    FindYourPokemonTheme {
        PokemonItem(
            dummyPokemons[2],
            {}
        )
    }
}