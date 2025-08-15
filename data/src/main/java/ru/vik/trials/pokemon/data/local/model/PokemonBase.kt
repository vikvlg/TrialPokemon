package ru.vik.trials.pokemon.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Базовая информация по покемону. */
@Entity(tableName = "pokemon")
internal data class PokemonBase(
    /** Идентификатор. */
    @PrimaryKey val id: Int,

    /** Имя. */
    val name: String,

    /** Ссылка для получения детализации по покемону. */
    val url: String,
)