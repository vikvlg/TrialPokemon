package ru.vik.trials.pokemon.ui.filters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.vik.trials.pokemon.databinding.FilterTypeItemLayoutBinding
import ru.vik.trials.pokemon.ui.model.PokemonType

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

    private lateinit var type: PokemonType
    private lateinit var onClickCallback: OnClickCallback

    fun setType(value: PokemonType) {
        type = value
    }

    fun setTypeName(value: String) {
        binding.type.text = value
    }

    fun updateSelected(value: Boolean) {
        binding.type.isChecked = value
    }

    fun setOnClickCallback(value: OnClickCallback) {
        onClickCallback = value
    }
}

typealias OnClickCallback = (type: PokemonType, active: Boolean) -> Unit
