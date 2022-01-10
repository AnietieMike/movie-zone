package com.anietie.moviezone.ui.movieslist.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.anietie.moviezone.R
import com.anietie.moviezone.data.local.model.Movie
import com.anietie.moviezone.data.local.model.RepoMoviesResult
import com.anietie.moviezone.data.local.model.Resource
import com.anietie.moviezone.data.repo.MovieRepository
import com.anietie.moviezone.ui.movieslist.MoviesFilterType
import kotlinx.coroutines.flow.MutableStateFlow
import java.lang.IllegalArgumentException

class DiscoverMoviesViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val repoMoviesResult: LiveData<RepoMoviesResult>
    var pagedList: LiveData<PagedList<Movie>>
    val networkState: LiveData<Resource<*>>
    private val currentTitle = MutableLiveData<Int?>()
    private val sortBy = MutableLiveData<MoviesFilterType?>()
    val currentSorting: MoviesFilterType?
        get() = sortBy.value

    fun getCurrentTitle(): LiveData<Int?> {
        return currentTitle
    }

    fun setSortMoviesBy(id: String) {
        val filterType: MoviesFilterType?
        val title: Int?
        when (id) {
            "Popular" -> {
                // check if already selected. no need to request API
                if (sortBy.value === MoviesFilterType.POPULAR) return
                filterType = MoviesFilterType.POPULAR
                title = R.string.action_popular
            }
            "Latest" -> {
                if (sortBy.value === MoviesFilterType.LATEST) return
                filterType = MoviesFilterType.LATEST
                title = R.string.action_latest
            }
            "Upcoming" -> {
                if (sortBy.value === MoviesFilterType.UPCOMING) return
                filterType = MoviesFilterType.UPCOMING
                title = R.string.action_upcoming
            }
            else -> throw IllegalArgumentException("unknown sorting id")
        }
        sortBy.value = filterType
        currentTitle.value = title
    }

    // retry any failed requests.
    fun retry() {
        repoMoviesResult.value!!.sourceLiveData.value!!.retryCallback!!.invoke()
    }

    init {
        // By default show popular movies
        sortBy.value = MoviesFilterType.POPULAR
        currentTitle.value = R.string.action_popular
        repoMoviesResult =
            Transformations.map(sortBy) { sort -> movieRepository.loadMoviesFilteredBy(sort) }
        pagedList = Transformations.switchMap(
            repoMoviesResult
        ) { (data) -> data }
        networkState = Transformations.switchMap(repoMoviesResult) { (_, resource) -> resource }
    }
}
