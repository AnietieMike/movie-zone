package com.anietie.moviezone.data.repo

import androidx.lifecycle.LiveData
import com.anietie.moviezone.data.local.MoviesLocalDataSource
import com.anietie.moviezone.data.local.model.Movie
import com.anietie.moviezone.data.local.model.MovieDetails
import com.anietie.moviezone.data.local.model.RepoMoviesResult
import com.anietie.moviezone.data.local.model.Resource
import com.anietie.moviezone.data.remote.MoviesRemoteDataSource
import com.anietie.moviezone.data.remote.api.ApiResponse
import com.anietie.moviezone.ui.movieslist.MoviesFilterType
import com.anietie.moviezone.utils.AppExecutors
import timber.log.Timber

/**
 * Repository implementation that returns a paginated data and loads data directly from network.
 *
 */
class MovieRepository private constructor(
    private val mLocalDataSource: MoviesLocalDataSource,
    private val mRemoteDataSource: MoviesRemoteDataSource,
    private val mExecutors: AppExecutors
) : DataSource {

    override fun loadMovie(movieId: Long): LiveData<Resource<MovieDetails>> {
        return object : NetworkBoundResource<MovieDetails, Movie>(mExecutors) {

            override fun saveCallResult(item: Movie) {
                mLocalDataSource.saveMovie(item)
                Timber.d("Movie added to database")
            }

            override fun shouldFetch(data: MovieDetails?): Boolean {
                Timber.tag("ShouldFetch").d("Fetching...")
                return data == null // only fetch fresh data if it doesn't exist in database
            }

            override fun loadFromDb(): LiveData<MovieDetails> {
                Timber.tag("LoadingFromDB").d("Loading movie from database")
                return mLocalDataSource.getMovie(movieId)
            }

            override fun createCall(): LiveData<ApiResponse<Movie>> {
                Timber.tag("LoadingMovieFromNetwork").d("Downloading movie from network")
                return mRemoteDataSource.loadMovie(movieId)
            }

            override fun onFetchFailed() {
                // ignored
                Timber.tag("FetchFailed").d("Fetch failed!!")
            }
        }.asLiveData
    }

    override fun loadMoviesFilteredBy(sortBy: MoviesFilterType?): RepoMoviesResult {
        Timber.tag("LoadMoviesListByFilters").d("Loading movie list from network")
        return mRemoteDataSource.loadMoviesFilteredBy(sortBy!!)
    }

    override val allFavoriteMovies: LiveData<List<Movie>>
        get() = mLocalDataSource.allFavoriteMovies

    override fun addToFavoriteMovie(movie: Movie) {
        mExecutors.diskIO().execute(
            Runnable {
                Timber.d("Adding movie to favorites")
                mLocalDataSource.favoriteMovie(movie)
            }
        )
    }

    override fun removeFromFavoriteMovie(movie: Movie) {
        mExecutors.diskIO().execute(
            Runnable {
                Timber.tag("RemoveFromFavorite").d("Removing movie from favorites")
                mLocalDataSource.unfavoriteMovie(movie)
            }
        )
    }

    companion object {
        @Volatile
        private lateinit var sInstance: MovieRepository
        fun getInstance(
            localDataSource: MoviesLocalDataSource,
            remoteDataSource: MoviesRemoteDataSource,
            executors: AppExecutors
        ): MovieRepository {
            synchronized(MovieRepository::class.java) {
                sInstance = MovieRepository(localDataSource, remoteDataSource, executors)
            }
            return sInstance
        }
    }
}
