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

    /** Получает список покемонов. */
    fun refresh() {
        Log.d("TAG", "[${Thread.currentThread().name}] refresh...")
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("TAG", "[${Thread.currentThread().name}] getPokemonListUseCase")

            // TODO: Из-за cachedIn код сценария выполняется в main-потоке,
            // было бы неплохо с этим разобраться.
            getPokemonListUseCase().cachedIn(viewModelScope).map { pagingData ->
                pagingData.filter {
                    //true
                    //(it.base.id % 10) == 9
                    val types = it.details?.types ?: ""
                    val isAvailable = (types.indexOf("poison") != -1)
                    if (!isAvailable) {
                        Log.d("TAG", "ignore #${it.base.id} ${it.base.name} = ${it.details?.types}")
                    }
                    isAvailable
                }
            }.collect {
                pokemonList.postValue(it)
            }

//            getPokemonListUseCase().cachedIn(viewModelScope).collect {
//                Log.d("TAG", "[${Thread.currentThread().name}] getPokemonListUseCase.collect")
//                pokemonList.postValue(it)
//            }
        }
    }
}