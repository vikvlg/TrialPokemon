package ru.vik.trials.pokemon.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.vik.trials.pokemon.domain.entities.Pokemon
import ru.vik.trials.pokemon.domain.entities.PokemonDetails
import ru.vik.trials.pokemon.domain.entities.Resp

interface PokemonRepository {
    fun getPokemonList(): Flow<PagingData<Pokemon>>
    fun getPokemon(id: Int): Flow<Resp<PokemonDetails>>
}