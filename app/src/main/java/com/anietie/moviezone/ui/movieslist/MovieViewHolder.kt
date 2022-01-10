package com.anietie.moviezone.ui.movieslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.anietie.moviezone.data.local.model.Movie
import com.anietie.moviezone.databinding.ItemMovieBinding
import com.anietie.moviezone.ui.movieslist.discover.DiscoverMoviesFragmentDirections

class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(
    binding.root
) {
    @UiThread
    fun bindTo(movie: Movie) {
        binding.movie = movie
        // movie click event
        binding.root.setOnClickListener { view ->
            val action = DiscoverMoviesFragmentDirections.actionDiscoverMoviesFragmentToMovieDetailsFragment(movie.id)
            view.findNavController().navigate(action)
        }
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): MovieViewHolder {
            // Inflate
            val layoutInflater = LayoutInflater.from(parent.context)
            // Create the binding
            val binding = ItemMovieBinding.inflate(layoutInflater, parent, false)
            return MovieViewHolder(binding)
        }
    }
}
