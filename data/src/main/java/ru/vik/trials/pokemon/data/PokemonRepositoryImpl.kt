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
import ru.vik.trials.pokemon.domain.entities.PokemonDetails
import ru.vik.trials.pokemon.domain.entities.Resp
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PokemonRepositoryImpl @Inject internal constructor(
    private val service: PokemonApi,
    private val database: AppDatabase,
) : PokemonRepository {
    companion object {
        private const val HTTP_ERROR_UNKNOWN = 308
    }

    override fun getPokemonList(): Flow<PagingData<Pokemon>> {
        return Pager(
            config = PagingConfig(
                PokemonApi.PAGE_SIZE,
                prefetchDistance = PokemonApi.PAGE_SIZE,
                initialLoadSize = PokemonApi.PAGE_SIZE,
            ),
            pagingSourceFactory = {
                Log.d("TAG", "pagingSourceFactory")
                database.getPokemonDao().getList()
            },
            remoteMediator = PokemonListRemoteMediator(service, database)
        ).flow.map { paging ->
            paging.map { it.toDomainPokemon() }
        }
    }

    override fun getPokemon(id: Int): Flow<Resp<PokemonDetails>> = flow {
        val dao = database.getPokemonDao()
        val pokemon = dao.get(id)
        if (pokemon?.sprite == null) {
            val response = service.getPokemon(id)
            val data = response.body()
            if (!response.isSuccessful || data == null) {
                Log.d("TAG", "getPokemon.error: ${response.message()}; code: ${response.code()}")
                Log.d("TAG", "   ${response.raw().request.url}")
                emit(Resp(HTTP_ERROR_UNKNOWN))
                return@flow
            }

            Log.d("TAG", "PokemonDetails insert...")
            dao.insert(data.toDbPokemon())
            Log.d("TAG", "PokemonDetails insert.OK")

            Log.d("TAG", data.name)
            for (stat in data.stats) {
                Log.d("TAG", "   ${stat.stat.name} = ${stat.value} / ${stat.effort} ")
            }
            for (slot in data.types) {
                Log.d("TAG", "   [${slot.slot}] ${slot.type.name}")
            }

            emit(Resp(data.toDomainPokemon()))
        }
        else {
            //emit(Resp(pokemon.toDomainPokemon()))
        }
    }
}
