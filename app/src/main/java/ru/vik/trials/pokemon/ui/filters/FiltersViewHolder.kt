package ru.vik.trials.pokemon.ui.filters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.vik.trials.pokemon.databinding.FilterTypeItemLayoutBinding
import ru.vik.trials.pokemon.ui.model.PokemonType

/** ViewHolder для элементов списка типов покемонов. */
class FiltersViewHolder(
    private val binding: FilterTypeItemLayoutBinding
): RecyclerView.ViewHolder(binding.root) {

    constructor(viewParent: ViewGroup)
            : this(FilterTypeItemLayoutBinding.inflate(LayoutInflater.from(viewParent.context), viewParent, false))

    init {
        binding.type.setOnCheckedChangeListener { obj, value ->
            onClickCallback(type, value)
        }
    }

    /** Тип покемона. */
    private lateinit var type: PokemonType

    /** Событие изменения активности. */
    private lateinit var onClickCallback: OnClickCallback

    /** Устанавливает тип покемона. */
    fun setType(value: PokemonType) {
        type = value
    }

    /** Устанавливает название типа. */
    fun setTypeName(value: String) {
        binding.type.text = value
    }

    /** Устанавливает активность типа. */
    fun setSelected(value: Boolean) {
        binding.type.isChecked = value
    }

    /** Устанавливает событие [onClickCallback]. */
    fun setOnClickCallback(value: OnClickCallback) {
        onClickCallback = value
    }
}

typealias OnClickCallback = (type: PokemonType, active: Boolean) -> Unit
