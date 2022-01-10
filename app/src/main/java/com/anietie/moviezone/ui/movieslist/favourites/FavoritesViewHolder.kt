package com.anietie.moviezone.ui.movieslist.favourites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.anietie.moviezone.data.local.model.Movie
import com.anietie.moviezone.databinding.ItemMovieBinding

class FavoritesViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(
    binding.root
) {
    @UiThread
    fun bindTo(movie: Movie) {
        binding.movie = movie
        // movie click event
        binding.root.setOnClickListener { view ->
            val action = FavouriteMoviesFragmentDirections.actionFavouriteMoviesFragmentToMovieDetailsFragment(movie.id)
            view.findNavController().navigate(action)
        }
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): FavoritesViewHolder {
            // Inflate
            val layoutInflater = LayoutInflater.from(parent.context)
            // Create the binding
            val binding = ItemMovieBinding.inflate(layoutInflater, parent, false)
            return FavoritesViewHolder(binding)
        }
    }
}