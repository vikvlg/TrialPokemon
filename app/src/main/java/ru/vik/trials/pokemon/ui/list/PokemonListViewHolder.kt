package ru.vik.trials.pokemon.ui.list

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.Job
import ru.vik.trials.pokemon.databinding.PokemonItemLayoutBinding


class PokemonListViewHolder(
    private val binding: PokemonItemLayoutBinding
) : RecyclerView.ViewHolder(binding.root) {

    constructor(viewParent: ViewGroup)
            : this(PokemonItemLayoutBinding.inflate(LayoutInflater.from(viewParent.context), viewParent, false))

    companion object {
        private var DEFAULT_CARD_STROKE_COLOR: Int = -1
        private var DEFAULT_CARD_STROKE_WIDTH: Int = -1
        private var SELECTED_CARD_STROKE_COLOR: Int = -1
        private var SELECTED_CARD_STROKE_WIDTH: Int = -1
    }

    init {
        // TRICKY: Запомним стандартные stroke-значения, чтобы можно было к ним откатываться
        if (DEFAULT_CARD_STROKE_COLOR == -1)
            DEFAULT_CARD_STROKE_COLOR = binding.card.strokeColor
        if (DEFAULT_CARD_STROKE_WIDTH == -1)
            DEFAULT_CARD_STROKE_WIDTH = binding.card.strokeWidth
        if (SELECTED_CARD_STROKE_COLOR == -1)
            SELECTED_CARD_STROKE_COLOR = ContextCompat.getColor(binding.root.context, com.google.android.material.R.color.design_default_color_primary)
        if (SELECTED_CARD_STROKE_WIDTH == -1)
            SELECTED_CARD_STROKE_WIDTH = (4 * binding.root.context.resources.displayMetrics.density).toInt()
    }

    /** Текущая задача по получению данных по покемону. */
    private var jobGetDetails: Job? = null

    /** Отображает имя покемона. */
    fun setName(value: String) {
        binding.pokemon.text = value
    }

    /** Выделяет покемона (при поиске). */
    fun setSelected(value: Boolean) {
        if (value) {
            binding.card.strokeColor = SELECTED_CARD_STROKE_COLOR
            binding.card.strokeWidth = SELECTED_CARD_STROKE_WIDTH
        }
        else {
            binding.card.strokeColor = DEFAULT_CARD_STROKE_COLOR
            binding.card.strokeWidth = DEFAULT_CARD_STROKE_WIDTH
        }
    }

    /** Отображает изображение покемона. */
    fun updateSprite(image: String?) {
        if (image == null)
            return

        val imageSize = 256
        Glide.with(binding.root)
            .load(image)
            .into(object : CustomTarget<Drawable?>(imageSize, imageSize) {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                    binding.pokemon.setCompoundDrawablesWithIntrinsicBounds(null, resource, null, null)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    binding.pokemon.setCompoundDrawablesWithIntrinsicBounds(null, placeholder, null, null)
                }
            })
    }

    /** Запоминает задачу по получению детализации по покомену. */
    fun setJobGetDetails(job: Job) {
        // Если предыдущая задача не завершилась, а holder перешел к другому элементу,
        // то необходимо отменить предыдущую задачу.
        if (jobGetDetails?.isActive == true)
            jobGetDetails?.cancel()

        jobGetDetails = job
    }
}