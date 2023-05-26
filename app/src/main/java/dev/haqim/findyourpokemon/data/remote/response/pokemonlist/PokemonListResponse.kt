package dev.haqim.findyourpokemon.data.remote.response.pokemonlist

import com.squareup.moshi.Json

data class PokemonListResponse(

	@field:Json(name="next")
	val nextUrl: String,

	@field:Json(name="previous")
	val previousUrl: String,

	@Json(name="count")
	val count: Int,

	@Json(name="results")
	val results: List<PokemonListItemResponse>
)

