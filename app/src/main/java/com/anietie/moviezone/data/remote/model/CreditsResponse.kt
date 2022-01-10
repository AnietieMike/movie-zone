package com.anietie.moviezone.data.remote.model

import com.anietie.moviezone.data.local.model.Cast
import com.google.gson.annotations.SerializedName

/**
 * @author Anietie Udoaka
 */
data class CreditsResponse(

    @SerializedName("cast")
    var cast: List<Cast>? = null
)
