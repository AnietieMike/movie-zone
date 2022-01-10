package com.anietie.moviezone.data.remote.model

import com.anietie.moviezone.data.local.model.Trailer
import com.google.gson.annotations.SerializedName

/**
 * @author Anietie Udoaka
 */
data class TrailersResponse(

    @SerializedName("results")
    var trailers: List<Trailer>? = null
)
