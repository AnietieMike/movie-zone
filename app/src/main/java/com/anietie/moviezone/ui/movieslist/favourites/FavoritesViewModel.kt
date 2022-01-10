package com.anietie.moviezone.ui.movieslist.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.anietie.moviezone.data.local.model.Movie
import com.anietie.moviezone.data.repo.MovieRepository

class FavoritesViewModel(repository: MovieRepository) : ViewModel() {

    //    private final MovieRepository movieRepository
    val favoriteListLiveData: LiveData<List<Movie>> = repository.allFavoriteMovies
}
