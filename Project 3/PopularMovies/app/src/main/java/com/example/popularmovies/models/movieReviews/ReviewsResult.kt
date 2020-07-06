package com.example.popularmovies.models.movieReviews

import com.google.gson.annotations.SerializedName

data class ReviewsResult(
    @SerializedName("results")
    var reviews: ArrayList<Review>
)