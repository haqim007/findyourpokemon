package dev.haqim.findyourpokemon.data.local.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.haqim.findyourpokemon.data.local.entity.POKEMON_MOVE_TABLE
import dev.haqim.findyourpokemon.data.local.entity.POKEMON_OWNED_TABLE
import dev.haqim.findyourpokemon.data.local.entity.POKEMON_TABLE
import dev.haqim.findyourpokemon.data.local.entity.POKEMON_TYPE_TABLE
import dev.haqim.findyourpokemon.data.local.entity.PokemonEntity
import dev.haqim.findyourpokemon.data.local.entity.PokemonOwnedEntity
import dev.haqim.findyourpokemon.data.local.entity.PokemonDetailEntity
import dev.haqim.findyourpokemon.data.local.entity.PokemonMoveEntity
import dev.haqim.findyourpokemon.data.local.entity.PokemonTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    
    @Upsert
    suspend fun upsertPokemons(pokemons: List<PokemonEntity>)
    
    @Upsert
    suspend fun upsertPokemonOwned(pokemonOwned: PokemonOwnedEntity)

    @Query("DELETE FROM $POKEMON_OWNED_TABLE WHERE pokemon_id = :pokemonId")
    suspend fun removePokemonOwned(pokemonId: String)

    @Transaction
    @Query("SELECT * FROM $POKEMON_TABLE")
    fun getAllPokemons(): PagingSource<Int, PokemonDetailEntity>

    @Transaction
    @Query("SELECT * FROM $POKEMON_TABLE WHERE id IN (SELECT pokemon_id FROM $POKEMON_OWNED_TABLE)")
    fun getAllOwnedPokemons(): PagingSource<Int, PokemonDetailEntity>

    @Transaction
    @Query("SELECT * FROM $POKEMON_TABLE where id = :id")
    fun getPokemonById(id: String): Flow<PokemonDetailEntity?>

    @Query("DELETE FROM $POKEMON_TABLE")
    suspend fun clearAllPokemons()

    @Insert
    suspend fun insertPokemonMoves(moves: List<PokemonMoveEntity>)

    @Insert
    suspend fun insertPokemonTypes(types: List<PokemonTypeEntity>)

    @Query("DELETE FROM $POKEMON_MOVE_TABLE where pokemon_id = :id")
    suspend fun clearAllPokemonMoves(id: String)
                                     
    @Query("DELETE FROM $POKEMON_TYPE_TABLE where pokemon_id = :id")
    suspend fun clearAllPokemonTypes(id: String)
    
    @Query("SELECT * FROM $POKEMON_MOVE_TABLE WHERE pokemon_id = :pokemonId")
    suspend fun getPokemonMoves(pokemonId: String): List<PokemonMoveEntity>

    @Query("SELECT * FROM $POKEMON_TYPE_TABLE WHERE pokemon_id = :pokemonId")
    suspend fun getPokemonTypes(pokemonId: String): List<PokemonTypeEntity>
    
}