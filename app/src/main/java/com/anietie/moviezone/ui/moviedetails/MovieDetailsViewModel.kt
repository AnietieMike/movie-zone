package com.anietie.moviezone.ui.moviedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.anietie.moviezone.R
import com.anietie.moviezone.data.local.model.MovieDetails
import com.anietie.moviezone.data.local.model.Resource
import com.anietie.moviezone.data.repo.MovieRepository
import com.anietie.moviezone.utils.SnackBarMessage

class MovieDetailsViewModel(private val repository: MovieRepository) : ViewModel() {
    var result: LiveData<Resource<MovieDetails>>? = null
    private val movieIdLiveData = MutableLiveData<Long>()
    val snackbarMessage = SnackBarMessage()
    private var isFavoriteLiveData = MutableLiveData<Boolean>()
    var isFavorite = false

    fun init(movieId: Long) {
        if (result != null) {
            return // load movie details only once the activity created first time
        }
        result = Transformations.switchMap(
            movieIdLiveData
        ) { id -> repository.loadMovie(id!!) }
        setMovieIdLiveData(movieId) // trigger loading movie
    }

    private fun setMovieIdLiveData(movieId: Long) {
        movieIdLiveData.value = movieId
    }

    fun retry(movieId: Long) {
        setMovieIdLiveData(movieId)
    }

    fun onFavoriteClicked() {
        val movieDetails = result!!.value?.data!!
        if (!isFavorite) {
            repository.addToFavoriteMovie(movieDetails.movie!!)
            isFavorite = true
            showSnackbarMessage(R.string.movie_added_successfully)
        } else {
            repository.removeFromFavoriteMovie(movieDetails.movie!!)
            isFavorite = false
            showSnackbarMessage(R.string.movie_removed_successfully)
        }
    }

    private fun showSnackbarMessage(message: Int) {
        snackbarMessage.value = message
    }
}
