package com.example.popularmovies.models.movieReviews

import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("author")
    var reviewWriter: String,
    var content: String
)