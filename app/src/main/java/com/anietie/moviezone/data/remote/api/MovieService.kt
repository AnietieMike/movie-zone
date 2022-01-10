package com.anietie.moviezone.data.remote.api

import androidx.lifecycle.LiveData
import com.anietie.moviezone.data.local.model.Movie
import com.anietie.moviezone.data.remote.model.MoviesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * TheMovieDB REST API access points.
 * <p>
 * @author Anietie Udoaka
 */
interface MovieService {
    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int): Call<MoviesResponse>

    @GET("movie/now_playing")
    fun getLatestMovies(@Query("page") page: Int): Call<MoviesResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(@Query("page") page: Int): Call<MoviesResponse>

    // Instead of using 4 separate requests we use append_to_response
    // to eliminate duplicate requests and save network bandwidth
    // this request return full movie details, trailers, reviews and cast
    @GET("movie/{id}?append_to_response=videos,credits,reviews")
    fun getMovieDetails(@Path("id") id: Long): LiveData<ApiResponse<Movie>>
}
