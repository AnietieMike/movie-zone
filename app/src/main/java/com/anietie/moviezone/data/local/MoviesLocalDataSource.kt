package com.anietie.moviezone.data.local

import androidx.lifecycle.LiveData
import com.anietie.moviezone.data.local.model.Cast
import com.anietie.moviezone.data.local.model.Movie
import com.anietie.moviezone.data.local.model.MovieDetails
import com.anietie.moviezone.data.local.model.Review
import com.anietie.moviezone.data.local.model.Trailer
import com.anietie.moviezone.utils.AppExecutors
import timber.log.Timber

/**
 * @author Anietie Udoaka
 */
class MoviesLocalDataSource(private val mDatabase: MoviesDatabase) {

    fun saveMovie(movie: Movie) {
        mDatabase.moviesDao().insertMovie(movie)
        insertTrailers(movie.trailersResponse?.trailers!!, movie.id)
        insertCastList(movie.creditsResponse?.cast!!, movie.id)
        insertReviews(movie.reviewsResponse?.reviews!!, movie.id)
    }

    private fun insertReviews(reviews: List<Review>, movieId: Long) {
        for (review in reviews) {
            review.movieId = movieId
        }
        mDatabase.reviewsDao().insertAllReviews(reviews)
        Timber.d("%s reviews inserted into database.", reviews.size)
    }

    private fun insertCastList(castList: List<Cast>, movieId: Long) {
        for (cast in castList) {
            cast.movieId = movieId
        }
        mDatabase.castsDao().insertAllCasts(castList)
        Timber.d("%s cast inserted into database.", castList.size)
    }

    private fun insertTrailers(trailers: List<Trailer>, movieId: Long) {
        for (trailer in trailers) {
            trailer.movieId = movieId
        }
        mDatabase.trailersDao().insertAllTrailers(trailers)
        Timber.d("%s trailers inserted into database.", trailers.size)
    }

    fun getMovie(movieId: Long): LiveData<MovieDetails> {
        Timber.d("Loading movie details.")
        return mDatabase.moviesDao().getMovie(movieId)
    }

    val allFavoriteMovies: LiveData<List<Movie>>
        get() = mDatabase.moviesDao().allFavoriteMovies

    fun favoriteMovie(movie: Movie) {
        mDatabase.moviesDao().favoriteMovie(movie.id)
    }

    fun unfavoriteMovie(movie: Movie) {
        mDatabase.moviesDao().unFavoriteMovie(movie.id)
    }

    companion object {
        @Volatile
        private lateinit var sInstance: MoviesLocalDataSource
        fun getInstance(database: MoviesDatabase): MoviesLocalDataSource {
            synchronized(AppExecutors::class.java) {
                sInstance = MoviesLocalDataSource(database)
            }
            return sInstance
        }
    }
}
