package ru.vik.trials.pokemon.data.remote.model

/** Ответ API со списком покемонов. */
internal class PokemonList(
    /** Количество покемонов в базе сервера. */
    val count: Int,
    /** Ссылка на предыдущую страницу. */
    val previous: String,
    /** Ссылка на следующую страницу. */
    val next: String?,
    /** Список покемонов. */
    val results: List<Pokemon>,
) {
    /** Базовая информация по покемону. */
    class Pokemon(
        /** Имя. */
        val name: String,
        /** Ссылка для получения детальной информации по покемону. */
        val url: String,
    )
}