package ru.vik.trials.pokemon.ui.list

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.vik.trials.pokemon.domain.GetPokemonListUseCase
import ru.vik.trials.pokemon.domain.entities.Pokemon
import ru.vik.trials.pokemon.ui.model.FilterData
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
): ViewModel() {

    companion object {
        private const val TAG = "PokemonListViewModel"
    }

    /** Список покемонов. */
    var pokemonList: MutableLiveData<PagingData<Pokemon>> = MutableLiveData()

    /** Фрагмент имени покемона для поиска. */
    val searchName = ObservableField("")

    var filter = FilterData()

    /** Получает список покемонов. */
    fun refresh() {
        Log.d(TAG, "[${Thread.currentThread().name}] refresh...")
        //Log.d("TAG", "   filter.typeMap: ${filter.typeMap}")
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "[${Thread.currentThread().name}] getPokemonListUseCase")

            // TODO: Из-за cachedIn код сценария выполняется в main-потоке,
            // было бы неплохо с этим разобраться.
            getPokemonListUseCase().cachedIn(viewModelScope).map { pagingData ->
                pagingData.filter {
                    filterItem(it)
                }
            }.collect {
                Log.d(TAG, "getPokemonListUseCase collect")
                pokemonList.postValue(it)
            }

//            getPokemonListUseCase().cachedIn(viewModelScope).collect {
//                Log.d("TAG", "[${Thread.currentThread().name}] getPokemonListUseCase.collect")
//                pokemonList.postValue(it)
//            }
        }
    }

    private fun filterItem(pokemon: Pokemon): Boolean {
        // Если по покемону не успели получить детализацию, то оставляем его в списке
        val details = pokemon.details ?: return true

        // Если все фильтры выключены, то считаем, что фильтр не установлен
        var allTypeFalse = false
        for ((_, value) in filter.typeMap) {
            allTypeFalse = allTypeFalse || value
        }
        //Log.d("TAG", "allTypeFalse: $allTypeFalse")
        //Log.d("TAG", "   filter.typeMap: ${filter.typeMap}")
        if (!allTypeFalse)
            return true

        // Проверим подходит ли покемон под указанный тип
        var available = false
        for ((type, value) in filter.typeMap) {
            if (!value)
                continue
            available = (details.types.indexOf(type.name, ignoreCase = true) != -1)
            if (available)
                break
        }

        if (!available)
            Log.d("TAG", "skip ${pokemon.base.name} type: ${pokemon.details?.types}")

        return available
    }
}