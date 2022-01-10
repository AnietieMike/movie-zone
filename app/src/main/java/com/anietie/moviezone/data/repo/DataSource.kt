package com.anietie.moviezone.data.repo

import androidx.lifecycle.LiveData
import com.anietie.moviezone.data.local.model.Movie
import com.anietie.moviezone.data.local.model.MovieDetails
import com.anietie.moviezone.data.local.model.RepoMoviesResult
import com.anietie.moviezone.data.local.model.Resource
import com.anietie.moviezone.ui.movieslist.MoviesFilterType

interface DataSource {
    fun loadMovie(movieId: Long): LiveData<Resource<MovieDetails>>
    fun loadMoviesFilteredBy(sortBy: MoviesFilterType?): RepoMoviesResult
    val allFavoriteMovies: LiveData<List<Movie>>
    fun addToFavoriteMovie(movie: Movie)
    fun removeFromFavoriteMovie(movie: Movie)
}
