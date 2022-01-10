package com.anietie.moviezone.data.remote.model

import com.anietie.moviezone.data.local.model.Movie
import com.google.gson.annotations.SerializedName

/**
 * MoviesResponse is a data class that parses the response from the ApiService
 */
data class MoviesResponse(

    @SerializedName("page")
    var page: Int = 0,

    @SerializedName("total_results")
    var totalResults: Int = 0,

    @SerializedName("total_pages")
    var totalPages: Int = 0,

    @SerializedName("results")
    var movies: List<Movie>? = null
)
