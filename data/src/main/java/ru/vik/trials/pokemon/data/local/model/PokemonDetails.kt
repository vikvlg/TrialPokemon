package ru.vik.trials.pokemon.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "details",
    foreignKeys = [
        ForeignKey(
            entity = PokemonBase::class,
            parentColumns = ["id"], childColumns = ["pokemonId"], onDelete = ForeignKey.NO_ACTION
        )
    ]
)
internal data class PokemonDetails(
    /** Идентификатор. */
    @PrimaryKey val pokemonId: Int,
    val sprite: String,
)