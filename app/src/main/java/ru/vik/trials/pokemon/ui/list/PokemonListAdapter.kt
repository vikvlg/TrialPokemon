package ru.vik.trials.pokemon.ui.list

import android.util.Log
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.vik.trials.pokemon.domain.GetPokemonUseCase
import ru.vik.trials.pokemon.domain.entities.Pokemon
import javax.inject.Inject

/** RecyclerView-адаптер для списка покемонов. */
class PokemonListAdapter @Inject constructor(
    private val getPokemonUseCase: GetPokemonUseCase
): PagingDataAdapter<Pokemon, PokemonListViewHolder>(POKEMON_COMPARATOR) {
    companion object {
        private const val TAG = "PokemonListAdapter"

        val POKEMON_COMPARATOR = object : DiffUtil.ItemCallback<Pokemon>() {
            override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                // TODO: Тут было бы неплохо проверять данные покемонов.
                oldItem.base.id == newItem.base.id
                //oldItem.details?.id == newItem.details?.id

            override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                oldItem.base.id == newItem.base.id

//            override fun getChangePayload(oldItem: BasePokemon, newItem: BasePokemon): Any? {
//                // Нас не интересует частичное изменение данных
//                return null
//            }
        }
    }

    /** Область для выполнения асинхронных задач. */
    lateinit var scope: CoroutineScope

    /** Индекс текущего найденого персонажа. */
    private var searchedItem: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonListViewHolder {
        return PokemonListViewHolder(parent)
    }

    override fun onBindViewHolder(holder: PokemonListViewHolder, position: Int) {
        val pokemon = getItem(position) ?: return
        holder.setName("${pokemon.base.id} ${pokemon.base.name}")
        holder.setSelected(position == searchedItem)
        //holder.updateSprite(null)

        // Если есть детализация, то утсановим изображение
        if (pokemon.details != null) {
            holder.updateSprite(pokemon.details?.sprite)
            return
        }

        // Получим детализацию по покемону, если ее нет
        val job = scope.launch (Dispatchers.IO) {
            Log.d(TAG, "[${Thread.currentThread().name}] getPokemonUseCase(${pokemon.base.id})")
            getPokemonUseCase(pokemon.base.id).collectLatest { resp ->
                if (resp.isSuccess) {
                    //Log.d("TAG", "!!! collect detail ${resp.value?.details?.sprite}")
                    withContext(Dispatchers.Main) {
                        holder.updateSprite(resp.value?.details?.sprite)
                    }
                }
                else {
                    Log.d("TAG", "!!! collect detail error ${resp.error}")
                }
            }
        }
        holder.setJobGetDetails(job)
    }

    /**
     * Возвращает идентификатор покемона.
     *
     * В [PagingDataAdapter] метод [getItemId] закрыт для изменений, пришлось создать новый.
     */
    fun getIdByPosition(position: Int): Int {
        val pokemon = getItem(position) ?: return RecyclerView.NO_ID.toInt()

        return pokemon.base.id
    }

    /**
     * Ищет покемона по совпадению имени.
     *
     * @param name Фрагмент имени для поиска.
     * @return Индекс найденного покемона, -1 - не найден.
     * */
    fun searchItemPosition(name: String): Int {
        var position = -1
        if (!name.isBlank()) {
            for (i in searchedItem + 1 until itemCount) {
                val item = getItem(i) ?: continue
                if (item.base.name.contains(name, true)) {
                    position = i
                    break
                }
            }
        }

        val prevPosition = searchedItem
        searchedItem = position
        // Обновим предыдущий найденный элемент и текущий,
        // чтобы сменилось выделение
        if (searchedItem != prevPosition) {
            notifyItemChanged(prevPosition)
            notifyItemChanged(searchedItem)
        }
        return searchedItem
    }
}
