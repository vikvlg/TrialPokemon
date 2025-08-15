package ru.vik.trials.pokemon.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.vik.trials.pokemon.data.remote.model.PokemonDetails
import ru.vik.trials.pokemon.data.remote.model.PokemonList

internal interface PokemonApi {
    companion object {
        /** Размер страницы со списком покемонов. */
        const val PAGE_SIZE = 20
    }

    /** Возвращает список покемонов. */
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<PokemonList>

    /** Возвращает детальную информацию по покемону. */
    @GET("pokemon/{id}/")
    suspend fun getPokemon(
        @Path("id")
        id: Int
    ): Response<PokemonDetails>
}