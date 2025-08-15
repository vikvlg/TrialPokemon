package ru.vik.trials.pokemon.ui.filters

import android.util.Log
import android.view.ViewGroup
import ru.vik.trials.pokemon.ui.common.RecyclerViewHolderHelper
import ru.vik.trials.pokemon.ui.model.PokemonType
import javax.inject.Inject

/** Адаптер для списка типов покемонов. */
class FiltersAdapter @Inject constructor(
): RecyclerViewHolderHelper<FiltersViewHolder>() {

    /** Типы покемонов и флаги активности типов. */
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
        holder.setSelected(typeList?.get(type) ?: false)
    }

    override fun getItemCount(): Int {
        return typeList?.size ?: 0
    }
}