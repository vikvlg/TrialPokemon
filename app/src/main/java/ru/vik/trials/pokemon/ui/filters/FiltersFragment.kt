package ru.vik.trials.pokemon.ui.filters

import android.os.Build
import android.os.Bundle
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

/** Фрагмент с установкой фильтров для списка покоменов. */
@AndroidEntryPoint
class FiltersFragment : Fragment() {

    private lateinit var binding: FragmentFiltersBinding
    @Inject lateinit var filtersAdapter: FiltersAdapter
    private val viewModel: FiltersViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFiltersBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        val filter =
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
            @Suppress("DEPRECATION")
            arguments?.getParcelable(Consts.KEY_FILTER_DATA)
        else
            arguments?.getParcelable(Consts.KEY_FILTER_DATA, FilterData::class.java)
        if (filter != null) {
            viewModel.filter = filter
            filtersAdapter.typeList = viewModel.filter.types
        }

        binding.typeList.adapter = filtersAdapter

        return binding.root
    }

    override fun onPause() {
        // Сохраним значения фильтров перед завершением фрагмента
        setFragmentResult(Consts.KEY_FILTER_DATA, bundleOf(Consts.KEY_FILTER_DATA to viewModel.filter))

        super.onPause()
    }
}