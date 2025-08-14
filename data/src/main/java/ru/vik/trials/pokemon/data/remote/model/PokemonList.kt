package ru.vik.trials.pokemon.data.remote.model

/** Ответ API со списком покемонов. */
internal class PokemonList(
    val count: Int,
    val previous: String,
    val next: String?,
    val results: List<Pokemon>,
) {
    /** Базовая информация по покемону. */
    class Pokemon(
        val name: String,
        val url: String,
    )
}