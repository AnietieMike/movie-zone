package com.anietie.moviezone.data.remote.model

import com.anietie.moviezone.data.local.model.Review
import com.google.gson.annotations.SerializedName

/**
 * @author Anietie Udoaka
 */
data class ReviewsResponse(

    @SerializedName("results")
    var reviews: List<Review>? = null
)
