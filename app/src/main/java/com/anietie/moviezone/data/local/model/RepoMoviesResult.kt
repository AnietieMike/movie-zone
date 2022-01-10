package com.anietie.moviezone.data.local.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.anietie.moviezone.data.remote.paging.MoviePageKeyedDataSource

data class RepoMoviesResult(
    val data: LiveData<PagedList<Movie>>,
    val resource: LiveData<Resource<*>>,
    val sourceLiveData: MutableLiveData<MoviePageKeyedDataSource>
)
