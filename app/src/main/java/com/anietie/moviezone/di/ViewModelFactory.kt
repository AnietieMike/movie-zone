package com.anietie.moviezone.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anietie.moviezone.data.repo.MovieRepository
import com.anietie.moviezone.ui.moviedetails.MovieDetailsViewModel
import com.anietie.moviezone.ui.movieslist.discover.DiscoverMoviesViewModel
import com.anietie.moviezone.ui.movieslist.favourites.FavoritesViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val repository: MovieRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DiscoverMoviesViewModel::class.java) -> {
                DiscoverMoviesViewModel(repository) as T
            }
            modelClass.isAssignableFrom(FavoritesViewModel::class.java) -> {
                FavoritesViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MovieDetailsViewModel::class.java) -> {
                MovieDetailsViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        fun getInstance(repository: MovieRepository): ViewModelFactory {
            return ViewModelFactory(repository)
        }
    }
}
