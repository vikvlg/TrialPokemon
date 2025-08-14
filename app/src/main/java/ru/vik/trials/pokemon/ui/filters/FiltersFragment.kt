package ru.vik.trials.pokemon.ui.filters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.vik.trials.pokemon.databinding.FragmentFiltersBinding
import ru.vik.trials.pokemon.ui.common.Consts
import ru.vik.trials.pokemon.ui.model.FilterData
import javax.inject.Inject

@AndroidEntryPoint
class FiltersFragment : Fragment() {

    private lateinit var binding: FragmentFiltersBinding
    @Inject lateinit var filtersAdapter: FiltersAdapter
    private val viewModel: FiltersViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFiltersBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        val filter = arguments?.getParcelable<FilterData>(Consts.KEY_FILTER_DATA)
        Log.d("TAG", "FiltersFragment filter: $filter")
        if (filter != null) {
            viewModel.filter = filter
            filtersAdapter.typeList = filter.typeMap
        }

        binding.typeList.adapter = filtersAdapter

        return binding.root
    }

    override fun onPause() {
        // Сохраним значения фильтров перед завершением фрагмента
        Log.d("TAG", "filter types: ${viewModel.filter.typeMap}")
        //Log.d("TAG", "filter: ${viewModel.filter.get()}")
        setFragmentResult(Consts.KEY_FILTER_DATA, bundleOf(Consts.KEY_FILTER_DATA to viewModel.filter))

        super.onPause()
    }
}