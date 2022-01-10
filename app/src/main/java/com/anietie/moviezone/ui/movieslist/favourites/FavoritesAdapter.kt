package com.anietie.moviezone.ui.movieslist.favourites

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anietie.moviezone.data.local.model.Movie

class FavoritesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mMoviesList: List<Movie>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FavoritesViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie: Movie = mMoviesList!![position]
        (holder as FavoritesViewHolder).bindTo(movie)
    }

    override fun getItemCount(): Int {
        return if (mMoviesList != null) mMoviesList!!.size else 0
    }

    fun submitList(movies: List<Movie>?) {
        mMoviesList = movies
        notifyDataSetChanged()
    }
}
