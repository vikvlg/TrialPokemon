package ru.vik.trials.pokemon.data

import ru.vik.trials.pokemon.data.local.model.PokemonTuple as DbPokemonTuple
import ru.vik.trials.pokemon.data.local.model.PokemonBase as DbPokemonBase
import ru.vik.trials.pokemon.data.local.model.PokemonDetails as DbPokemonDetails
import ru.vik.trials.pokemon.data.remote.model.PokemonDetails as ApiPokemonDetails
import ru.vik.trials.pokemon.domain.entities.Pokemon as DomainPokemon
import ru.vik.trials.pokemon.domain.entities.PokemonBase as DomainPokemonBase
import ru.vik.trials.pokemon.domain.entities.PokemonDetails as DomainPokemonDetails

internal fun DbPokemonBase.toDomainPokemon(): DomainPokemonBase {
    return DomainPokemonBase(id, name)
}

internal fun DbPokemonDetails.toDomainPokemon(): DomainPokemonDetails {
    return DomainPokemonDetails(id = pokemonId, sprite = sprite, stats = stats, types = types)
}

internal fun DbPokemonTuple.toDomainPokemon(): DomainPokemon {
    return DomainPokemon(pokemon.toDomainPokemon(), details?.toDomainPokemon())
}

internal fun ApiPokemonDetails.toDbPokemon(): DbPokemonDetails {
    return DbPokemonDetails(
        id,
        sprites.frontDefault ?: "",
        stats.joinToString(separator = "\n", transform = { "${it.stat.name} = ${it.value}" }),
        types.joinToString(separator = "\n", transform = { it.type.name })
    )
}
