package ru.vik.trials.pokemon.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.vik.trials.pokemon.domain.entities.BasePokemon
import ru.vik.trials.pokemon.domain.entities.DetailPokemon

interface PokemonRepository {
    fun getPokemonList(): Flow<PagingData<BasePokemon>>
    fun getPokemon(id: Int): Flow<DetailPokemon>
}