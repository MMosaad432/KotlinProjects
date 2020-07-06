package com.example.popularmovies.models.movieTrailers

import com.google.gson.annotations.SerializedName

data class Trailer(
    @SerializedName("key")
    var thumbnailKey: String,
    @SerializedName("name")
    var trailerName: String
)