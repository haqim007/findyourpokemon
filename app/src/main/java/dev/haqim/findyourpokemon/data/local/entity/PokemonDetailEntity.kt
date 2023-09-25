package dev.haqim.findyourpokemon.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import dev.haqim.findyourpokemon.domain.model.Pokemon

data class PokemonDetailEntity(
    @Embedded
    val pokemon: PokemonEntity,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "pokemon_id"
    )
    val pokemonOwned: PokemonOwnedEntity? = null,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "pokemon_id",
    )
    val pokemonTypes: List<PokemonTypeEntity>,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "pokemon_id"
    )
    val pokemonMoves: List<PokemonMoveEntity>,
){
    val isOwned: Boolean get() = pokemonOwned != null
}

fun PokemonDetailEntity.toModel() = Pokemon(
    id = this.pokemon.id,
    name = this.pokemon.name,
    nickname = this.pokemonOwned?.nickname,
    avatarUrl = this.pokemon.avatarUrl,
    isOwned = this.isOwned,
    type = this.pokemonTypes.map { it.typeName },
    move = this.pokemonMoves.map { it.moveName },
)

fun List<PokemonDetailEntity>.toModel() = this.map { it.toModel() }