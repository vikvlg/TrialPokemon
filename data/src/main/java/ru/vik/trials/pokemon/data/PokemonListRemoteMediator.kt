package ru.vik.trials.pokemon.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import ru.vik.trials.pokemon.data.local.AppDatabase
import ru.vik.trials.pokemon.data.local.model.PokemonBase
import ru.vik.trials.pokemon.data.local.model.PokemonTuple
import ru.vik.trials.pokemon.data.remote.PokemonApi
import java.io.IOException
import javax.inject.Inject

private const val BASE_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
internal class PokemonListRemoteMediator @Inject constructor(
    private val service: PokemonApi,
    private val database: AppDatabase,
) : RemoteMediator<Int, PokemonTuple>() {
    companion object {
        private const val PAGE_SIZE = PokemonApi.PAGE_SIZE
    }

    override suspend fun initialize(): InitializeAction {
        Log.d("TAG", "[${Thread.currentThread().name}] RemoteMediator initialize")
        return super.initialize()
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, PokemonTuple>): MediatorResult {
        Log.d("TAG", "[${Thread.currentThread().name}] RemoteMediator load; loadType: $loadType")
        return try {
            val pageNumber =
                when (loadType) {
                    LoadType.REFRESH -> {
                        // Если в БД есть подходящие данные, то ничего не делаем
                        if (getDbPokemonCount() >= PAGE_SIZE)
                            return MediatorResult.Success(endOfPaginationReached = false)

                        // В БД данных нет, загрузим их с первой страницы
                        BASE_STARTING_PAGE_INDEX
                    }

                    LoadType.PREPEND -> {
                        // Мы грузим данные последовательно, так что PREPEND нет смысла обрабатывать.
                        return MediatorResult.Success(endOfPaginationReached = false)
                    }
                    LoadType.APPEND -> {
                        // Узнаем сколько уже данных загружено и
                        // определим какую следующую страницу необходимо подгрузить
                        val page = state.pages.lastOrNull()
                        Log.d("CharactersRemoteMediator", "   APPEND lastPage: $page")
                        val pageNumber = if (page == null) {
                            BASE_STARTING_PAGE_INDEX
                        }
                        else {
                            // В БД еще есть данные, ничего не делаем
                            if (page.itemsAfter > PAGE_SIZE)
                                return MediatorResult.Success(endOfPaginationReached = false)

                            val loadedItems = page.itemsBefore + page.data.size + page.itemsAfter
                            loadedItems / PAGE_SIZE + 1
                        }

                        // Специальная проверка для первой страницы
                        if (pageNumber == BASE_STARTING_PAGE_INDEX && getDbPokemonCount() >= PAGE_SIZE)
                            return MediatorResult.Success(endOfPaginationReached = false)

                        pageNumber
                    }
                }

            // Загрузим данные из API
            Log.d("CharactersRemoteMediator", "   !!!!!!!!loadKey: $pageNumber")
            val response = service.getPokemonList((pageNumber - 1) * PAGE_SIZE, PAGE_SIZE)
            val data = response.body() ?: return MediatorResult.Error(Exception("No data"))
            // Запишем полученные данные в БД
            Log.d("TAG", "PokemonBase insert[${data.results.size}]...")
            database.withTransaction {
                val dao = database.getPokemonDao()
                for (pokemon in data.results) {
                    // TRICKY: Т.к. API не возвращает идентификатор, будем выдергиваеть его из ссылки.
                    val id = pokemon.url.split('/').last { !it.isBlank() }
                    //Log.d("TAG", "pokemon: [$id] ${pokemon.name} from ${pokemon.url}")
                    dao.insert(PokemonBase(id.toInt(), pokemon.name, pokemon.url))
                }
            }
            Log.d("TAG", "PokemonBase insert.OK")

            // Установим флаг, есть ли еще данные в API
            val endOfPaginationReached = data.next.isNullOrEmpty()
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    suspend fun getDbPokemonCount(): Int {
        return database.withTransaction {
            database.getPokemonDao().getCount()
        }
    }
}