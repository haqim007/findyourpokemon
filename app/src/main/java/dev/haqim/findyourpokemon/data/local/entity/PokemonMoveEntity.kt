package dev.haqim.findyourpokemon.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

const val POKEMON_MOVE_TABLE = "pokemon_move"
@Entity(
    tableName = POKEMON_MOVE_TABLE,
    foreignKeys = [
        ForeignKey(
            entity = PokemonEntity::class,
            parentColumns = ["id"],
            childColumns = ["pokemon_id"],
            onDelete = ForeignKey.NO_ACTION
        )
    ]
)
data class PokemonMoveEntity(
    @PrimaryKey(autoGenerate = true) 
    val id: Int = 0,
    @ColumnInfo(name = "pokemon_id")
    val pokemonId: String,
    @ColumnInfo(name = "move_name")
    val moveName: String
)