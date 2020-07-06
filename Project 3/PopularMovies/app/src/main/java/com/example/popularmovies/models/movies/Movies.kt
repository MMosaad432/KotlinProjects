package com.example.popularmovies.models.movies

import com.google.gson.annotations.SerializedName

data class Movies(
    @SerializedName("results")
    var movies: ArrayList<Movie>
)