package ru.vik.trials.pokemon.ui.details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import dagger.hilt.android.AndroidEntryPoint
import ru.vik.trials.pokemon.databinding.FragmentPokemonDetailsBinding
import ru.vik.trials.pokemon.domain.entities.PokemonDetails
import ru.vik.trials.pokemon.ui.common.Consts
import kotlin.getValue

@AndroidEntryPoint
class PokemonDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPokemonDetailsBinding
    private val viewModel: PokemonDetailsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPokemonDetailsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        // Обработчик изменений детализации по покмену
        viewModel.details.addOnPropertyChangedCallback(object: Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {
                val details = (sender as ObservableField<*>).get() as? PokemonDetails ?: return

                // Устаноим изображение покемона
                val imageSize = 512
                Glide.with(binding.root)
                    .load(details.sprite)
                    .into(object : CustomTarget<Drawable?>(imageSize, imageSize) {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                            binding.pokemon.setCompoundDrawablesWithIntrinsicBounds(null, resource, null, null)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            binding.pokemon.setCompoundDrawablesWithIntrinsicBounds(null, placeholder, null, null)
                        }
                    })
            }
        })

        val pokemonId = arguments?.getInt(Consts.KEY_POKEMON_ID)
        if (pokemonId != null)
            viewModel.refresh(pokemonId)

        return binding.root
    }
}