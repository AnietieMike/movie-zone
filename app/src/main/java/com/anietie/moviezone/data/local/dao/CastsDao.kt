package com.anietie.moviezone.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.anietie.moviezone.data.local.model.Cast

/**
 * @author Anietie Udoaka
 */
@Dao
interface CastsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllCasts(castList: List<Cast>)
}