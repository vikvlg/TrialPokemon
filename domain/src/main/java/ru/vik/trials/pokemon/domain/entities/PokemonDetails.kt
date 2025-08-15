package ru.vik.trials.pokemon.domain.entities

/** Дополнительная информация по покемону. */
data class PokemonDetails(
    /** Идентификатор. */
    val id: Int = -1,

    /** Ссылка на изображение. */
    val sprite: String = "",

    /** Список параметров. */
    val stats: String = "",

    /** Список типов. */
    val types: String = "",
)