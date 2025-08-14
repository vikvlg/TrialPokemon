package ru.vik.trials.pokemon.ui.list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.vik.trials.pokemon.databinding.FragmentPokemonListBinding
import ru.vik.trials.pokemon.domain.entities.Pokemon
import ru.vik.trials.pokemon.ui.common.CommonLoadStateAdapter
import javax.inject.Inject

@AndroidEntryPoint
class PokemonListFragment
    : Fragment() {

    companion object {
        private const val TAG = "PokemonListFragment"
    }

    private lateinit var binding: FragmentPokemonListBinding
    private val viewModel: PokemonListViewModel by viewModels()
    @Inject lateinit var adapter: PokemonListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPokemonListBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        adapter.scope = lifecycleScope

        val concatAdapter = adapter.withLoadStateFooter(
            footer = CommonLoadStateAdapter {
                adapter.retry()
            }
        )

        binding.pokemonList.adapter = concatAdapter
        adapter.addLoadStateListener { loadStates ->
            binding.pokemonList.isVisible = loadStates.refresh !is LoadState.Loading
            binding.charactersLoader.isVisible = loadStates.refresh is LoadState.Loading
            binding.emptyState.isVisible = loadStates.refresh !is LoadState.Loading && adapter.itemCount == 0
        }

        // Установим ширину футера в 2 столбца
        val layoutManager = binding.pokemonList.layoutManager
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    // Если позиция элемента больше, чем есть данных в оригинальном адаптере,
                    // значит это футер.
                    return if (position >= adapter.itemCount) 2 else 1
                }
            }
        }

        viewModel.pokemonList.observe(viewLifecycleOwner, Observer<PagingData<Pokemon>> {
            Log.d(TAG, "pokemonList observe")
            adapter.submitData(lifecycle, it)
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        // Данные запрашиваем здесь, чтобы успел среагировать FragmentResultListener.
        viewModel.refresh()
    }
}