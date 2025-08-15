package ru.vik.trials.pokemon.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.vik.trials.pokemon.domain.entities.Pokemon
import ru.vik.trials.pokemon.domain.entities.Resp

/** Репозиторий для работы с покемонами. */
interface PokemonRepository {
    /** Получает список покемонов. */
    fun getPokemonList(): Flow<PagingData<Pokemon>>

    /** Получает детальную информацию по покемону. */
    fun getPokemon(id: Int): Flow<Resp<Pokemon>>
}