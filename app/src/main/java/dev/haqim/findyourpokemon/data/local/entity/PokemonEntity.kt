package dev.haqim.findyourpokemon.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val POKEMON_TABLE = "pokemon"
@Entity(tableName = POKEMON_TABLE)
data class PokemonEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String,
    @ColumnInfo(name = "is_owned") val isOwned: Boolean = false
)