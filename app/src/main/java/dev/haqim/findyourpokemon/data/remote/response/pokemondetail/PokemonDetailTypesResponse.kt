package dev.haqim.findyourpokemon.data.remote.response.pokemondetail

import com.squareup.moshi.Json

data class PokemonDetailTypesResponse(

	@field:Json(name="slot")
	val slot: Int,

	@field:Json(name="type")
	val type: Type
)