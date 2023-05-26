package dev.haqim.findyourpokemon.data.remote.response.pokemondetail

import com.squareup.moshi.Json
import dev.haqim.findyourpokemon.data.local.entity.PokemonMoveEntity
import dev.haqim.findyourpokemon.data.local.entity.PokemonTypeEntity

data class PokemonDetailResponse(

	@field:Json(name="location_area_encounters")
	val locationAreaEncounters: String,

	@field:Json(name="types")
	val types: List<PokemonDetailTypesResponse>,

	@field:Json(name="base_experience")
	val baseExperience: Int,

	@field:Json(name="held_items")
	val heldItems: List<Any>,

	@field:Json(name="weight")
	val weight: Int,

	@field:Json(name="is_default")
	val isDefault: Boolean,

	@field:Json(name="past_types")
	val pastTypes: List<Any>,

	@field:Json(name="sprites")
	val sprites: Sprites,

	@field:Json(name="species")
	val species: Species,

	@field:Json(name="moves")
	val moves: List<MovesItem>,

	@field:Json(name="name")
	val name: String,

	@field:Json(name="id")
	val id: Int,

	@field:Json(name="height")
	val height: Int,

	@field:Json(name="order")
	val order: Int
)


data class Sprites(

	@field:Json(name="back_shiny_female")
	val backShinyFemale: String,

	@field:Json(name="back_female")
	val backFemale: String,

	@field:Json(name="back_default")
	val backDefault: String,

	@field:Json(name="front_shiny_female")
	val frontShinyFemale: String,

	@field:Json(name="front_default")
	val frontDefault: String,

	@field:Json(name="front_female")
	val frontFemale: String,

	@field:Json(name="back_shiny")
	val backShiny: String,

	@field:Json(name="front_shiny")
	val frontShiny: String
)

data class Species(

	@field:Json(name="name")
	val name: String,

	@field:Json(name="url")
	val url: String
)

data class Move(

	@field:Json(name="name")
	val name: String,

	@field:Json(name="url")
	val url: String
)

data class MovesItem(
	@field:Json(name="move")
	val move: Move
)

data class Type(

	@field:Json(name="name")
	val name: String,

	@field:Json(name="url")
	val url: String
)

fun PokemonDetailResponse.toPokemonTypesEntity(pokemonId: String) = 
	this.types.map { 
		PokemonTypeEntity(pokemonId = pokemonId, typeName = it.type.name)
	}

fun PokemonDetailResponse.toPokemonMovesEntity(pokemonId: String) =
	this.moves.map {
		PokemonMoveEntity(pokemonId = pokemonId, moveName = it.move.name)
	}