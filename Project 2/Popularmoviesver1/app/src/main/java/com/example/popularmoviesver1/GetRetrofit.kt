package com.example.popularmoviesver1

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GetRetrofit {

    val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}