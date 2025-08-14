package ru.vik.trials.pokemon.ui.list

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.vik.trials.pokemon.R
import ru.vik.trials.pokemon.databinding.FragmentPokemonListBinding
import ru.vik.trials.pokemon.domain.entities.Pokemon
import ru.vik.trials.pokemon.ui.common.CommonLoadStateAdapter
import ru.vik.trials.pokemon.ui.common.Consts
import ru.vik.trials.pokemon.ui.common.ItemClickSupport
import ru.vik.trials.pokemon.ui.model.FilterData
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

        // Слушатель изменения фильтров
        setFragmentResultListener(Consts.KEY_FILTER_DATA) { _, bundle ->
            val filter =
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
                    @Suppress("DEPRECATION")
                    bundle.getParcelable(Consts.KEY_FILTER_DATA)
                else
                    bundle.getParcelable(Consts.KEY_FILTER_DATA, FilterData::class.java)

            if (filter != null) {
                if (filter == viewModel.filter) {
                    Log.d(TAG, "filter data no change: ${viewModel.filter} = $filter")
                }
                else {
                    Log.d(TAG, "new filter data: ${viewModel.filter} -> $filter")
                    Log.d("TAG", "   filter.typeMap: ${filter.typeMap}")
                    viewModel.filter = filter
                    viewModel.refresh()
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        addMenu()
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

        // Обработчики поиска покемона
        binding.searchName.setOnClickListener {
            doSearchClick()
        }
        binding.searchingName.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                doSearchClick()
                return@OnKeyListener true
            }
            false
        })

        // Обработчик нажатия по персонажу
        with(ItemClickSupport.addTo(binding.pokemonList)) {
            setOnItemClickListener { _, position, _ -> doPokemonClick(position) }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        // Данные запрашиваем здесь, чтобы успел среагировать FragmentResultListener.
        viewModel.refresh()
    }

    private fun addMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_pokemon_list, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_filters -> {
                        doFiltersClick()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    /** Ищет следующего персонажа */
    private fun doSearchClick() {
        val position = adapter.searchItemPosition(viewModel.searchName.get() ?: "")
        if (position == -1) {
            Toast.makeText(context, resources.getString(R.string.pokemon_search_done), Toast.LENGTH_SHORT).apply {
                setGravity(Gravity.TOP, 0, 16)
                show()
            }
        }
        else
            binding.pokemonList.layoutManager?.scrollToPosition(position)
    }

    /**
     * Обработчик клика по покемону.
     *
     * Переход к фрагменту с детализацией персонажа.
     * */
    private fun doPokemonClick(position: Int) {
        Log.d(TAG, "doClick at $position")
        val id = adapter.getIdByPosition(position)
        if (id == RecyclerView.NO_ID.toInt())
            return

        val navController = NavHostFragment.findNavController(this)
        navController.navigate(R.id.PokemonDetailsFragment, bundleOf(Consts.KEY_POKEMON_ID to id))
    }

    private fun doFiltersClick() {
        val navController = NavHostFragment.findNavController(this)
        navController.navigate(R.id.FilterFragment, bundleOf(Consts.KEY_FILTER_DATA to viewModel.filter))
    }

}
