package ru.vik.trials.pokemon.ui.filters

import android.util.Log
import android.view.ViewGroup
import ru.vik.trials.pokemon.ui.common.RecyclerViewHolderHelper
import ru.vik.trials.pokemon.ui.model.PokemonType
import javax.inject.Inject

class FiltersAdapter @Inject constructor(
): RecyclerViewHolderHelper<FiltersViewHolder>() {

    var typeList: MutableMap<PokemonType, Boolean>? = null

    /** Индекс выбранного типа. */
    private var selectedIndex: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiltersViewHolder {
        val vh = FiltersViewHolder(parent)
        vh.setOnClickCallback { type, value ->
            Log.d("TAG", "$type = $value")
            typeList?.set(type, value)
        }
        return vh
    }

    override fun onBindViewHolder(holder: FiltersViewHolder, position: Int) {
        val type = typeList?.keys?.toList()[position] ?: return
        holder.setType(type)
        holder.setTypeName(type.name)
        holder.updateSelected(typeList?.get(type) ?: false)
    }

    override fun getItemCount(): Int {
        return typeList?.size ?: 0
    }

    /**
     * Выделяет пункт с мелодией.
     * @param position Индекс мелодии.
     */
    fun select(position: Int) {
        selectedIndex = position
        getHolderAtPosition(position)?.updateSelected(true)

        for (index in getHolderCount() - 1 downTo 0) {
            val holder = getHolderAtIndex(index) ?: continue
            if (position != holder.adapterPosition)
                holder.updateSelected(false)
        }
    }
}