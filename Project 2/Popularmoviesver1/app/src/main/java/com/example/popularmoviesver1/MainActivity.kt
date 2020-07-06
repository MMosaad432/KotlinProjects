package com.example.popularmoviesver1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.wessam.library.NetworkChecker
import com.wessam.library.NoInternetLayout
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() , MoviesRecyclerViewAdapter.MovieImageOnClickListener{
    var moviesArrayList: ArrayList<Movie> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!NetworkChecker.isNetworkConnected(this)){
            NoInternetLayout.Builder(this, R.layout.activity_main).animate()
        }else {
            var gridLayoutManager: GridLayoutManager =
                GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false)
            val retrofitService: RetrofitService = GetRetrofit.retrofit.create(RetrofitService::class.java)
            var moviesCall: Call<Movies> = retrofitService.getMoviesByPopularity()
            moviesCall.enqueue(object : Callback<Movies> {

                override fun onFailure(call: Call<Movies>, t: Throwable) {
                    Log.i("WMWMWMWWWMWWMWMWMWM", t.message)
                }

                override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                    Log.i("WMWMWMWWWMWWMWMWMWM", "YOOOU Succeeded")
                    var movies: Movies? = response.body()
                    var arr1: ArrayList<Movie> = movies!!.movies!!
                    moviesArrayList = arr1
                    var arr2: ArrayList<String> = arrayListOf()
                    for (pathing in arr1) {
                        arr2.add(pathing.posterPath!!)
                    }
                    moviesRecyclerView.layoutManager = gridLayoutManager
                    moviesRecyclerView.adapter =
                        MoviesRecyclerViewAdapter(this@MainActivity, this@MainActivity, arr2)
                }

            })
        }

    }

    override fun onClicked(pos: Int) {
        startActivity(Intent(this@MainActivity,DetailsActivity::class.java).putExtra(Constants.MOVIE_OBJECT,moviesArrayList.get(pos)))
    }
}
