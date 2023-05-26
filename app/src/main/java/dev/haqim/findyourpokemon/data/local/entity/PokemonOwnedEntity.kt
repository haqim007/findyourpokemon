package dev.haqim.findyourpokemon.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

const val POKEMON_OWNED_TABLE = "pokemon_owned"
@Entity(
    tableName = POKEMON_OWNED_TABLE,
    foreignKeys = [
        ForeignKey(
            entity = PokemonEntity::class,
            parentColumns = ["id"],
            childColumns = ["pokemon_id"],
            onDelete = ForeignKey.NO_ACTION
        )
    ]
)
data class PokemonOwnedEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "pokemon_id") 
    val pokemonId: String,
    @ColumnInfo(name = "nickname") val nickname: String? = null
)