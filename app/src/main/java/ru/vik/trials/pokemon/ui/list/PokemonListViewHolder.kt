package ru.vik.trials.pokemon.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
import ru.vik.trials.pokemon.R
import ru.vik.trials.pokemon.databinding.PokemonItemLayoutBinding
import ru.vik.trials.pokemon.ui.common.setDrawableTop

class PokemonListViewHolder(
    private val binding: PokemonItemLayoutBinding
) : RecyclerView.ViewHolder(binding.root) {

    constructor(viewParent: ViewGroup)
            : this(PokemonItemLayoutBinding.inflate(LayoutInflater.from(viewParent.context), viewParent, false))

    /** Текущая задача по получению данных по покемону. */
    private var jobGetDetails: Job? = null

    /** Отображает имя покемона. */
    fun setName(value: String) {
        binding.pokemon.text = value
    }

    /** Отображает изображение покемона. */
    fun updateSprite(image: ByteArray?) {
        binding.pokemon.setDrawableTop(image, R.drawable.pokemon_unk)
    }

    /** Запоминает задачу по получению данных по покомену. */
    fun setJobGetDetails(job: Job) {
        // Если предыдущая задача не завершилась, а holder перешел к другому элементу,
        // то необходимо отменить предыдущую задачу.
        if (jobGetDetails?.isActive == true)
            jobGetDetails?.cancel()

        jobGetDetails = job
    }
}