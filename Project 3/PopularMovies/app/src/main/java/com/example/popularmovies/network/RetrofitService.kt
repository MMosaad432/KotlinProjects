package com.example.popularmovies.network

import com.example.popularmovies.models.movieReviews.ReviewsResult
import com.example.popularmovies.models.movieTrailers.TrailersResult
import com.example.popularmovies.models.movies.Movies
import com.example.popularmovies.utils.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {

    @GET(Constants.POPULARITY+ Constants.API_KEY)
    fun getMoviesByPopularity(@Query("page")pageNumber:String) : Call<Movies>
    @GET(Constants.TOP_RATED+ Constants.API_KEY)
    fun getMoviesByTopRated(@Query("page")pageNumber:String) : Call<Movies>
    @GET("{id}/"+ Constants.VIDEOS + Constants.API_KEY)
    fun getMovieTrailers(@Path("id") movieId : Int) : Call<TrailersResult>
    @GET("{id}/"+ Constants.REVIEWS + Constants.API_KEY)
    fun getMovieReviews(@Path("id") movieId : Int) : Call<ReviewsResult>

}