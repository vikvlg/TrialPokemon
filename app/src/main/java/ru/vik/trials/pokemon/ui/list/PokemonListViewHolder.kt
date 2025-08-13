package ru.vik.trials.pokemon.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.vik.trials.pokemon.R
import ru.vik.trials.pokemon.databinding.PokemonItemLayoutBinding
import ru.vik.trials.pokemon.ui.common.setDrawableTop

class PokemonListViewHolder(
    private val binding: PokemonItemLayoutBinding
) : RecyclerView.ViewHolder(binding.root) {

    constructor(viewParent: ViewGroup)
            : this(PokemonItemLayoutBinding.inflate(LayoutInflater.from(viewParent.context), viewParent, false))

    /** Отображает имя покемона. */
    fun setName(value: String) {
        binding.pokemon.text = value
    }

    /** Отображает изображение покемона. */
    fun updateSprite(image: ByteArray?) {
        binding.pokemon.setDrawableTop(image, R.drawable.pokemon_unk)
    }
}