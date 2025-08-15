package ru.vik.trials.pokemon.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

/** Кортеж данных по покемону. */
internal data class PokemonTuple(
    /** Базовая информация. */
    @Embedded
    val pokemon: PokemonBase,

    /** Дополнительная информация. */
    @Relation(parentColumn = "id", entityColumn = "pokemonId")
    val details: PokemonDetails? = null
)
