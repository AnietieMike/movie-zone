package com.anietie.moviezone.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.anietie.moviezone.data.local.model.Trailer

/**
 * @author Anietie Udooaka
 */
@Dao
interface TrailersDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllTrailers(trailers: List<Trailer>)
}
