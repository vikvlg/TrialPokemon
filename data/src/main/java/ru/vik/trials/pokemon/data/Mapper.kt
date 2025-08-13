package ru.vik.trials.pokemon.data

import ru.vik.trials.pokemon.data.local.model.Pokemon as DbPokemon
import ru.vik.trials.pokemon.data.remote.model.PokemonList
import ru.vik.trials.pokemon.domain.entities.BasePokemon as DomainPokemon

internal fun DbPokemon.toDomainPokemon(): DomainPokemon {
    return DomainPokemon(id ?: -1, name, url)
}

//internal fun PokemonList.Pokemon.toDbPokemon(): DbPokemon {
//    return DbPokemon(id, "", url)
//}
