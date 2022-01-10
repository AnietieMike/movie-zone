package com.anietie.moviezone.data.remote

import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.anietie.moviezone.data.local.model.Movie
import com.anietie.moviezone.data.local.model.RepoMoviesResult
import com.anietie.moviezone.data.local.model.Resource
import com.anietie.moviezone.data.remote.api.ApiResponse
import com.anietie.moviezone.data.remote.api.MovieService
import com.anietie.moviezone.data.remote.paging.MovieDataSourceFactory
import com.anietie.moviezone.ui.movieslist.MoviesFilterType
import com.anietie.moviezone.utils.AppExecutors

/**
 * @author Anietie Udoaka
 */
class MoviesRemoteDataSource(
    private val mMovieService: MovieService,
    private val mExecutors: AppExecutors
) {
    fun loadMovie(movieId: Long): LiveData<ApiResponse<Movie>> {
        return mMovieService.getMovieDetails(movieId)
    }

    /**
     * Load movies for certain filter.
     */
    fun loadMoviesFilteredBy(sortBy: MoviesFilterType): RepoMoviesResult {
        val sourceFactory = MovieDataSourceFactory(mMovieService, mExecutors.networkIO(), sortBy)

        // paging configuration
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PAGE_SIZE)
            .build()

        // Get the paged list
        val moviesPagedList: LiveData<PagedList<Movie>> =
            LivePagedListBuilder(sourceFactory, config)
                .setFetchExecutor(mExecutors.networkIO())
                .build()

        val networkState: LiveData<Resource<*>> = Transformations.switchMap(
            sourceFactory.sourceLiveData,
            Function { input -> input.networkState }
        )

        // Get pagedList and network errors exposed to the viewmodel
        return RepoMoviesResult(
            moviesPagedList,
            networkState,
            sourceFactory.sourceLiveData
        )
    }

    companion object {
        private const val PAGE_SIZE = 20
        @Volatile
        private lateinit var sInstance: MoviesRemoteDataSource
        fun getInstance(
            movieService: MovieService,
            executors: AppExecutors
        ): MoviesRemoteDataSource {
            synchronized(AppExecutors::class.java) {
                sInstance = MoviesRemoteDataSource(movieService, executors)
            }
            return sInstance
        }
    }
}
