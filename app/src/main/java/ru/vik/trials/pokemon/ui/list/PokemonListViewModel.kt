package ru.vik.trials.pokemon.ui.list

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vik.trials.pokemon.domain.GetPokemonListUseCase
import ru.vik.trials.pokemon.domain.entities.BasePokemon
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
): ViewModel() {

    companion object {
        private const val TAG = "PokemonListViewModel"
    }

    /** Список покемонов. */
    var pokemonList: MutableLiveData<PagingData<BasePokemon>> = MutableLiveData()

    /** Фрагмент имени покемона для поиска. */
    val searchName = ObservableField("")

    /** Получает список покемонов. */
    fun refresh() {
        Log.d(TAG, "refresh...")
        viewModelScope.launch(Dispatchers.IO) {
            getPokemonListUseCase().cachedIn(viewModelScope).collect {
                Log.d("TAG", "[${Thread.currentThread().name}] CharacterListViewModel refresh")
                pokemonList.postValue(it)
            }
        }
    }
}