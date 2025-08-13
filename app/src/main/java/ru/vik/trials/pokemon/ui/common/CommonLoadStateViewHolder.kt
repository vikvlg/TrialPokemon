package ru.vik.trials.pokemon.ui.common

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import ru.vik.trials.pokemon.databinding.LoadStateFooterViewItemBinding

class CommonLoadStateViewHolder(
    private val binding: LoadStateFooterViewItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener {
            Log.d("TAG", "retryButton click")
            retry.invoke()
        }
    }

    fun bind(loadState: LoadState) = with(binding) {
        if (loadState is LoadState.Error) {
            errorMsg.text = loadState.error.localizedMessage
        }

        progressBar.isVisible = loadState is LoadState.Loading
        retryButton.isVisible = loadState is LoadState.Error
        errorMsg.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): CommonLoadStateViewHolder {
            val binding = LoadStateFooterViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CommonLoadStateViewHolder(binding, retry)
        }
    }
}