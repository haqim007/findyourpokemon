package dev.haqim.findyourpokemon.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

const val POKEMON_TYPE_TABLE = "pokemon_type"
@Entity(
    tableName = POKEMON_TYPE_TABLE,
    foreignKeys = [
        ForeignKey(
            entity = PokemonEntity::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.NO_ACTION
        )
    ]
)
data class PokemonTypeEntity(
    @PrimaryKey(autoGenerate = true) 
    val id: Int = 0,
    @ColumnInfo(name = "pokemon_id")
    val pokemonId: String,
    @ColumnInfo(name = "type_name")
    val typeName: String
)