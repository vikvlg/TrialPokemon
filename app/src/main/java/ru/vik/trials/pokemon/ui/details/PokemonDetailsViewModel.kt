package ru.vik.trials.pokemon.ui.details

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.vik.trials.pokemon.domain.GetPokemonUseCase
import ru.vik.trials.pokemon.domain.entities.PokemonDetails
import javax.inject.Inject

/** ViewModel для детализации по покемону. */
@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private val getPokemonUseCase: GetPokemonUseCase
): ViewModel() {

    companion object {
        private const val TAG = "PokemonDetailsViewModel"
    }

    /** Идентификатор покемона. */
    var id: Int = -1

    /** Имя покемона. */
    val name = ObservableField("")

    /** Дополнительная ифномарция по покемону. */
    val details = ObservableField(PokemonDetails())

    /** Получает деатльную информацию по покемону. */
    fun refresh(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getPokemonUseCase(id).collectLatest { resp ->
                //Log.d(TAG, "getPokemonUseCase collect")
                val pokemon = resp.value
                if (!resp.isSuccess || pokemon == null) {
                    name.set("Error code: ${resp.error}")
                    return@collectLatest
                }

                name.set(pokemon.base.name)
                details.set(pokemon.details)
            }
        }
    }
}