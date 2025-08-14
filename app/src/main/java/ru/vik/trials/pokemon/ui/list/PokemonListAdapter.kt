package ru.vik.trials.pokemon.ui.list

import android.util.Log
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.vik.trials.pokemon.domain.GetPokemonUseCase
import ru.vik.trials.pokemon.domain.entities.Pokemon
import javax.inject.Inject

class PokemonListAdapter @Inject constructor(
    private val getPokemonUseCase: GetPokemonUseCase
): PagingDataAdapter<Pokemon, PokemonListViewHolder>(POST_COMPARATOR) {
    companion object {
        private const val TAG = "PokemonListAdapter"
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<Pokemon>() {
            override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                // TODO: Тут было бы неплохо проверять данные покемонов.
                oldItem.details?.id == newItem.details?.id

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonListViewHolder {
        return PokemonListViewHolder(parent)
    }

    override fun onBindViewHolder(holder: PokemonListViewHolder, position: Int) {
        val pokemon = getItem(position) ?: return
        holder.setName("${pokemon.base.id} ${pokemon.base.name}")
        holder.updateSprite(null)

        if (pokemon.details != null) {
            // TODO: Получить изображение покемона.
            return
        }

        // Получим детализацию по покемону, если ее нет
        val job = scope.launch (Dispatchers.IO) {
            Log.d("TAG", "!!! getPokemonUseCase")
            getPokemonUseCase(pokemon.base.id).collectLatest { resp ->
                if (resp.isSuccess) {
                    Log.d("TAG", "!!! collect detail ${resp.value?.sprite}")
                    // TODO: Получить изображение покемона.
                }
                else {
                    Log.d("TAG", "!!! collect detail error ${resp.error}")
                }
            }
        }
        holder.setJobGetDetails(job)
    }
}
