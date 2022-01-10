package com.anietie.moviezone.data.local.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * @author Anietie Udoaka
 */
object Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromGenresList(genres: List<Genre>): String {
        return gson.toJson(genres)
    }

    @TypeConverter
    fun toGenresList(genres: String?): List<Genre> {
        if (genres == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<Genre>>() {}.type
        return gson.fromJson(genres, listType)
    }
}
