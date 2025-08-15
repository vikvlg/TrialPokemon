package ru.vik.trials.pokemon.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.vik.trials.pokemon.data.local.AppDatabase
import ru.vik.trials.pokemon.data.remote.PokemonApi
import ru.vik.trials.pokemon.domain.PokemonRepository
import ru.vik.trials.pokemon.domain.entities.Pokemon
import ru.vik.trials.pokemon.domain.entities.Resp
import javax.inject.Inject

/** Репозиторий по работе с покемонами. */
@OptIn(ExperimentalPagingApi::class)
class PokemonRepositoryImpl @Inject internal constructor(
    private val service: PokemonApi,
    private val database: AppDatabase,
) : PokemonRepository {
    companion object {
        private const val HTTP_ERROR_UNKNOWN = 308
        private const val TAG = "PokemonRepository"
    }

    override fun getPokemonList(): Flow<PagingData<Pokemon>> {
        //Log.d(TAG, "[${Thread.currentThread().name}] getPokemonList")

        return Pager(
            config = PagingConfig(
                PokemonApi.PAGE_SIZE,
                prefetchDistance = PokemonApi.PAGE_SIZE * 3,
                initialLoadSize = PokemonApi.PAGE_SIZE * 3,
            ),
            pagingSourceFactory = {
                //Log.d(TAG, "[${Thread.currentThread().name}] pagingSourceFactory")
                database.getPokemonDao().getList()
            },
            remoteMediator = PokemonListRemoteMediator(service, database)
        ).flow.map { paging ->
            paging.map { it.toDomainPokemon() }
        }
    }

    override fun getPokemon(id: Int): Flow<Resp<Pokemon>> = flow {
        try {
            val dao = database.getPokemonDao()
            val cachedPokemon = dao.get(id)

            // В БД нет даже базовой записи по покемону, значит с идентификатором что-то не так
            if (cachedPokemon == null) {
                throw Exception("No base pokemon #$id")
            }

            val pokemon =
                if (cachedPokemon.details != null) {
                    cachedPokemon
                }
                else {
                    // В БД нет детализации, запросим ее
                    val response = service.getPokemon(id)
                    val data = response.body()
                    if (!response.isSuccessful || data == null) {
                        throw Exception("${response.message()} code: ${response.code()}")
                    }

                    dao.insert(data.toDbPokemon())

                    val dbPokemon = dao.get(id)
                    if (dbPokemon == null || dbPokemon.details == null)
                        throw Exception("No detail pokemon #$id")

                    dbPokemon
                }
            emit(Resp(pokemon.toDomainPokemon()))
        }
        catch (ex: Exception) {
            Log.d(TAG, "getPokemon($id) error: ${ex.message}")
            emit(Resp(HTTP_ERROR_UNKNOWN))
        }
    }
}
