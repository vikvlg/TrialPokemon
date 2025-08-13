package ru.vik.trials.pokemon.ui.common

import android.util.Log
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class CommonLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<CommonLoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup, loadState: LoadState
    ): CommonLoadStateViewHolder {
        Log.d("TAG", "onCreateViewHolder")
        return CommonLoadStateViewHolder.create(parent, retry)
    }

    override fun onBindViewHolder(holder: CommonLoadStateViewHolder, loadState: LoadState) {
        Log.d("TAG", "onBindViewHolder")
        holder.bind(loadState)
    }
}