package com.example.popularmoviesver1

import retrofit2.Call
import retrofit2.http.GET

interface RetrofitService {

    @GET(Constants.POPULARITY+Constants.API_KEY)
    fun getMoviesByPopularity() : Call<Movies>
    @GET(Constants.TOP_RATED+Constants.API_KEY)
    fun getMoviesByTopRated() : Call<Movies>

}