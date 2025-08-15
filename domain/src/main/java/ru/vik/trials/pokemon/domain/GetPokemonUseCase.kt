package ru.vik.trials.pokemon.domain

import javax.inject.Inject

/** Сценарий получения детальной информации по покемону. */
class GetPokemonUseCase @Inject constructor(
    private val repository: PokemonRepository
) {

    operator fun invoke(id: Int) = repository.getPokemon(id)
}