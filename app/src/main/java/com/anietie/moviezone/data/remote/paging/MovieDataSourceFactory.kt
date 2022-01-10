package com.anietie.moviezone.data.remote.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.anietie.moviezone.data.local.model.Movie
import com.anietie.moviezone.data.remote.api.MovieService
import com.anietie.moviezone.ui.movieslist.MoviesFilterType
import java.util.concurrent.Executor

/**
 * A simple data source factory provides a way to observe the last created data source.
 *
 * @author Yassin Ajdi.
 */
class MovieDataSourceFactory(
    private val movieService: MovieService,
    private val networkExecutor: Executor,
    private val sortBy: MoviesFilterType
) : DataSource.Factory<Int, Movie>() {
    var sourceLiveData: MutableLiveData<MoviePageKeyedDataSource> =
        MutableLiveData<MoviePageKeyedDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MoviePageKeyedDataSource(movieService, networkExecutor, sortBy)
        sourceLiveData.postValue(movieDataSource)
        return movieDataSource
    }
}
