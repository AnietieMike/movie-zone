package com.anietie.moviezone.data.local.model

import com.google.gson.annotations.SerializedName

/**
 * @author Anietie Udoaka
 */
data class Genre(

    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("name")
    var name: String? = null
)
