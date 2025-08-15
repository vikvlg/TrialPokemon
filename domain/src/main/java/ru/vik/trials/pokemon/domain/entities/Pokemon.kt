package ru.vik.trials.pokemon.domain.entities

/** Объединенные данные по покемону. */
data class Pokemon(
    /** Базовая информация. */
    val base: PokemonBase,

    /** Дополнительная информация. */
    val details: PokemonDetails?
)