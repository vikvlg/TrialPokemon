package ru.vik.trials.pokemon.ui.filters

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.vik.trials.pokemon.ui.model.FilterData
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(

): ViewModel() {

    var filter: FilterData = FilterData()
}