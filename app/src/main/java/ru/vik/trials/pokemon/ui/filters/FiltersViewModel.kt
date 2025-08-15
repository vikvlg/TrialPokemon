package ru.vik.trials.pokemon.ui.filters

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.vik.trials.pokemon.ui.model.FilterData
import javax.inject.Inject

/** ViewModel для фильтров списка покемонов. */
@HiltViewModel
class FiltersViewModel @Inject constructor(

): ViewModel() {

    /** Текущий установленный фильтр. */
    var filter: FilterData = FilterData()
}