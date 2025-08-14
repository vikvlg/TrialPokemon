package ru.vik.trials.pokemon.domain.entities

data class Pokemon(
    val base: PokemonBase,
    val details: PokemonDetails?
)