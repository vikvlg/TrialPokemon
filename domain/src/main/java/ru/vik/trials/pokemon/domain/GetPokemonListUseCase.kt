package ru.vik.trials.pokemon.domain

import javax.inject.Inject

/** Сценарий получения списка покемонов. */
class GetPokemonListUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    operator fun invoke() = repository.getPokemonList()
}