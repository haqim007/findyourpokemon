package dev.haqim.findyourpokemon.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.haqim.findyourpokemon.data.local.entity.PokemonEntity
import dev.haqim.findyourpokemon.data.local.entity.PokemonMoveEntity
import dev.haqim.findyourpokemon.data.local.entity.PokemonOwnedEntity
import dev.haqim.findyourpokemon.data.local.entity.PokemonTypeEntity
import dev.haqim.findyourpokemon.data.local.entity.RemoteKeys
import dev.haqim.findyourpokemon.data.local.room.dao.PokemonDao
import dev.haqim.findyourpokemon.data.local.room.dao.RemoteKeysDao


@Database(
    version = 7,
    entities = [
        RemoteKeys::class,
        PokemonEntity::class,
        PokemonOwnedEntity::class,
        PokemonMoveEntity::class,
        PokemonTypeEntity::class
    ],
    exportSchema = false
)
abstract class PokemonDatabase: RoomDatabase() {
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun pokemonDao(): PokemonDao
}