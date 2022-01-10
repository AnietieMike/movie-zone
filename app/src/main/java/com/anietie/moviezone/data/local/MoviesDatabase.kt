package com.anietie.moviezone.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.anietie.moviezone.data.local.dao.CastsDao
import com.anietie.moviezone.data.local.dao.MoviesDao
import com.anietie.moviezone.data.local.dao.ReviewsDao
import com.anietie.moviezone.data.local.dao.TrailersDao
import com.anietie.moviezone.data.local.model.Cast
import com.anietie.moviezone.data.local.model.Converters
import com.anietie.moviezone.data.local.model.Movie
import com.anietie.moviezone.data.local.model.Review
import com.anietie.moviezone.data.local.model.Trailer

/**
 * The Room Database that manages a local database.
 *
 */
@Database(
    entities = [Movie::class, Trailer::class, Cast::class, Review::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(
    Converters::class
)

abstract class MoviesDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
    abstract fun trailersDao(): TrailersDao
    abstract fun castsDao(): CastsDao
    abstract fun reviewsDao(): ReviewsDao

    companion object {
        const val DATABASE_NAME = "Movies.db"
        private lateinit var INSTANCE: MoviesDatabase
        private val sLock = Any()
        fun getInstance(context: Context): MoviesDatabase {
            synchronized(sLock) {
                INSTANCE = buildDatabase(context)
                return INSTANCE
            }
        }

        private fun buildDatabase(context: Context): MoviesDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                MoviesDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}
