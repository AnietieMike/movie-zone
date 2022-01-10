package com.anietie.moviezone.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.anietie.moviezone.data.local.model.Review

/**
 * @author Anietie Udoaka
 */
@Dao
interface ReviewsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllReviews(reviews: List<Review>)
}
