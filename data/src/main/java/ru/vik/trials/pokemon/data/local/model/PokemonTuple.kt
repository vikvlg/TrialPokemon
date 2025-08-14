package ru.vik.trials.pokemon.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

//internal class PokemonTuple {
//    @Embedded
//    var pokemon: PokemonBase? = null
//
//    @Relation(parentColumn = "id", entityColumn = "pokemonId")
//    var details: PokemonDetails? = null
//}

internal data class PokemonTuple(
    @Embedded
    val pokemon: PokemonBase,

    @Relation(parentColumn = "id", entityColumn = "pokemonId")
    val details: PokemonDetails? = null
)
