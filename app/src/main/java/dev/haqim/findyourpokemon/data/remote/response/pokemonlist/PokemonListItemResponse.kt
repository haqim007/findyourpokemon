package dev.haqim.findyourpokemon.data.remote.response.pokemonlist

import com.squareup.moshi.Json
import dev.haqim.findyourpokemon.data.local.entity.PokemonEntity

data class PokemonListItemResponse(

	@Json(name="name")
	val name: String,

	@Json(name="url")
	val url: String
)

fun List<PokemonListItemResponse>.toEntity(): List<PokemonEntity> = 
	this.map {
		val urlParts = it.url.split("/")
		val id = urlParts[urlParts.size - 2]	
		PokemonEntity(
			id = id,
			name = it.name,
			avatarUrl = 
				"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png",
		)
	}