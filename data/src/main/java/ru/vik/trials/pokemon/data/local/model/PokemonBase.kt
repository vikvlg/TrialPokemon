package ru.vik.trials.pokemon.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

//@Entity(
//    tableName = "pokemon",
//    indices = [Index(
//        value = ["name"],
//        unique = true
//    )]
//)

@Entity(tableName = "pokemon",
)
internal data class PokemonBase(
    /** Идентификатор. */
    @PrimaryKey val id: Int,

    /** Имя. */
    val name: String,

    /** Ссылка для получения детализации по покемону. */
    val url: String,

    val sprite: String? = null
)