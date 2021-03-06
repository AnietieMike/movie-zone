package com.anietie.moviezone.ui.movieslist.discover

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anietie.moviezone.R
import com.anietie.moviezone.data.local.model.Movie
import com.anietie.moviezone.data.local.model.Resource
import com.anietie.moviezone.ui.movieslist.MovieViewHolder
import com.anietie.moviezone.ui.movieslist.discover.NetworkStateViewHolder.Companion.create

/**
 * Adapter implementation that shows a list of Movie posters
 *
 */
class DiscoverMoviesAdapter(private val mViewModel: DiscoverMoviesViewModel) :
    PagedListAdapter<Movie, RecyclerView.ViewHolder>(
        MOVIE_COMPARATOR
    ) {

    private var resource: Resource<*>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_movie -> MovieViewHolder.create(parent)
            R.layout.item_network_state -> create(parent, mViewModel)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_movie -> {
                (holder as MovieViewHolder).bindTo(
                    getItem(position)!!
                )
//                holder.itemView.setOnClickListener {
//                    val action = DiscoverMoviesFragmentDirections.actionDiscoverMoviesFragmentToMovieDetailsFragment(getItem(position)!!.id)
//                    it.findNavController().navigate(action)
//                }
            }
            R.layout.item_network_state -> (holder as NetworkStateViewHolder).bindTo(resource!!)
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.item_network_state
        } else {
            R.layout.item_movie
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    private fun hasExtraRow(): Boolean {
        return resource != null && resource!!.status !== Resource.Status.SUCCESS
    }

    fun setNetworkState(resource: Resource<*>) {
        val previousState = this.resource
        val hadExtraRow = hasExtraRow()
        this.resource = resource
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != resource) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {
        private val MOVIE_COMPARATOR: DiffUtil.ItemCallback<Movie> =
            object : DiffUtil.ItemCallback<Movie>() {
                override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
