package ru.vik.trials.pokemon.ui.list

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import ru.vik.trials.pokemon.domain.entities.BasePokemon
import javax.inject.Inject

class PokemonListAdapter @Inject constructor(
): PagingDataAdapter<BasePokemon, PokemonListViewHolder>(POST_COMPARATOR) {
    companion object {
        private const val TAG = "PokemonListAdapter"
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<BasePokemon>() {
            override fun areContentsTheSame(oldItem: BasePokemon, newItem: BasePokemon): Boolean =
                // TODO: Тут было бы неплохо проверять данные покемонов.
                oldItem.id == newItem.id

            override fun areItemsTheSame(oldItem: BasePokemon, newItem: BasePokemon): Boolean =
                oldItem.id == newItem.id

//            override fun getChangePayload(oldItem: BasePokemon, newItem: BasePokemon): Any? {
//                // Нас не интересует частичное изменение данных
//                return null
//            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonListViewHolder {
        return PokemonListViewHolder(parent)
    }

    override fun onBindViewHolder(holder: PokemonListViewHolder, position: Int) {
        val pokemon = getItem(position) ?: return
        holder.setName("${pokemon.id} ${pokemon.name}")
        holder.updateSprite(null)
        // TODO: Получить детализацию (изображение) по покемону.
        // TODO: Получить изображение покемона.
    }
}
