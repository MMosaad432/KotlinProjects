package com.example.popularmovies.models.movieTrailers

import com.google.gson.annotations.SerializedName

data class TrailersResult(
    @SerializedName("results")
    var trailers: ArrayList<Trailer>
)