package ru.vik.trials.pokemon.ui.common

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

/** Адаптер для футера RecyclerView. */
class CommonLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<CommonLoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): CommonLoadStateViewHolder {
        return CommonLoadStateViewHolder.create(parent, retry)
    }

    override fun onBindViewHolder(holder: CommonLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}