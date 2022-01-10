package com.anietie.moviezone.ui.movieslist.discover

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anietie.moviezone.data.local.model.Resource
import com.anietie.moviezone.databinding.ItemNetworkStateBinding

/**
 * A View Holder that can display a loading or have click action.
 * It is used to show the network state of paging.
 *
 */
class NetworkStateViewHolder(
    private val binding: ItemNetworkStateBinding,
    private val viewModel: DiscoverMoviesViewModel
) : RecyclerView.ViewHolder(binding.root) {
    fun bindTo(resource: Resource<*>) {
        binding.resource = resource
        binding.executePendingBindings()
    }

    companion object {
        @JvmStatic
        fun create(parent: ViewGroup, viewModel: DiscoverMoviesViewModel): NetworkStateViewHolder {
            // Inflate
            val layoutInflater = LayoutInflater.from(parent.context)
            // Create the binding
            val binding = ItemNetworkStateBinding.inflate(layoutInflater, parent, false)
            return NetworkStateViewHolder(binding, viewModel)
        }
    }

    init {
        // Trigger retry event on click
        binding.retryButton.setOnClickListener { viewModel.retry() }
    }
}
