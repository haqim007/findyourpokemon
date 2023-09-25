package dev.haqim.findyourpokemon.data.local

import androidx.room.withTransaction
import dev.haqim.findyourpokemon.data.local.entity.PokemonEntity
import dev.haqim.findyourpokemon.data.local.entity.PokemonMoveEntity
import dev.haqim.findyourpokemon.data.local.entity.PokemonOwnedEntity
import dev.haqim.findyourpokemon.data.local.entity.PokemonTypeEntity
import dev.haqim.findyourpokemon.data.local.entity.RemoteKeys
import dev.haqim.findyourpokemon.data.local.room.PokemonDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor (private val database: PokemonDatabase) {

    private val remoteKeysDao = database.remoteKeysDao()
    private val pokemonDao = database.pokemonDao()

    suspend fun clearRemoteKeys() = remoteKeysDao.clearRemoteKeys()
    suspend fun insertRemoteKeys(keys: List<RemoteKeys>) = remoteKeysDao.insertAll(keys)
    suspend fun getRemoteKeysById(id: String) = remoteKeysDao.getRemoteKeyById(id)

    
   
    suspend fun upsertAllPokemons(pokemons: List<PokemonEntity>) = pokemonDao.upsertPokemons(pokemons)
    suspend fun upsertPokemonOwned(pokemon: PokemonOwnedEntity) = pokemonDao.upsertPokemonOwned(pokemon)
    suspend fun removePokemonOwned(pokemonId: String) = pokemonDao.removePokemonOwned(pokemonId)
    fun getAllPokemons() = pokemonDao.getAllPokemons()
    suspend fun getAllOwnedPokemons(limit: Int, offset: Int) = pokemonDao.getAllOwnedPokemons(limit, offset)
    fun getPokemonById(id: String) = pokemonDao.getPokemonById(id)
    private suspend fun clearAllPokemons() = pokemonDao.clearAllPokemons()

    private suspend fun insertPokemonMoves(moves: List<PokemonMoveEntity>) = pokemonDao.insertPokemonMoves(moves)
    private suspend fun insertPokemonTypes(types: List<PokemonTypeEntity>) = pokemonDao.insertPokemonTypes(types)
    private suspend fun deleteAllPokemonMoves(pokemonId: String) = pokemonDao.clearAllPokemonMoves(pokemonId)
    private suspend fun deleteAllPokemonTypes(pokemonId: String) = pokemonDao.clearAllPokemonTypes(pokemonId)

    suspend fun upsertPokemonTypesAndMoves(
        pokemonId: String,
        moves: List<PokemonMoveEntity>,
        types: List<PokemonTypeEntity>
    ){
        database.withTransaction {
            deleteAllPokemonMoves(pokemonId)
            insertPokemonMoves(moves)
            deleteAllPokemonTypes(pokemonId)
            insertPokemonTypes(types)
        }
    }

    suspend fun insertKeysAndPokemons(
        keys: List<RemoteKeys>,
        pokemons: List<PokemonEntity>,
        isRefresh: Boolean = false
    ){
        database.withTransaction {
            if (isRefresh) {
                clearRemoteKeys()
                clearAllPokemons()
            }
            insertRemoteKeys(keys)
            upsertAllPokemons(pokemons)
        }
    }
    
}