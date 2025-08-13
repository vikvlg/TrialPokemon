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
import ru.vik.trials.pokemon.domain.entities.BasePokemon
import ru.vik.trials.pokemon.domain.entities.DetailPokemon
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PokemonRepositoryImpl @Inject internal constructor(
    private val service: PokemonApi,
    private val database: AppDatabase,
) : PokemonRepository {
    override fun getPokemonList(): Flow<PagingData<BasePokemon>> {
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

    override fun getPokemon(id: Int): Flow<DetailPokemon> = flow {
    }
}
