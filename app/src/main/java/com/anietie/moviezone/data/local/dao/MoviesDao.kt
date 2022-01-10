package com.anietie.moviezone.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.anietie.moviezone.data.local.model.Movie
import com.anietie.moviezone.data.local.model.MovieDetails

/**
 * @author Anietie Udoaka.
 */
@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovie(movie: Movie)

    @Transaction
    @Query("SELECT * FROM movie WHERE movie.id= :movieId")
    fun getMovie(movieId: Long): LiveData<MovieDetails>

    @get:Query("SELECT * FROM movie WHERE is_favorite = 1")
    val allFavoriteMovies: LiveData<List<Movie>>

    /**
     * Favorite a movie.
     *
     * @return the number of movies updated. This should always be 1.
     */
    @Query("UPDATE movie SET is_favorite = 1 WHERE id = :movieId")
    fun favoriteMovie(movieId: Long): Int

    /**
     * Unfavorite a movie.
     *
     * @return the number of movies updated. This should always be 1.
     */
    @Query("UPDATE movie SET is_favorite = 0 WHERE id = :movieId")
    fun unFavoriteMovie(movieId: Long): Int
}
