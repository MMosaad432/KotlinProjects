package com.example.popularmovies.screens.MainActivity

import android.app.Application
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.popularmovies.database.AppDatabase
import com.example.popularmovies.database.getInstance
import com.example.popularmovies.models.movies.Movie
import com.example.popularmovies.models.movies.Movies
import com.example.popularmovies.network.RetrofitInstance
import com.example.popularmovies.network.RetrofitService
import com.example.popularmovies.utils.Constants
import com.wessam.library.NetworkChecker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var moviesPopularityArrayList: ArrayList<Movie> = ArrayList()
    var moviesTopRatedArrayList: ArrayList<Movie> = ArrayList()
    var pathsPopularityArrayList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    var pathsTopRatedArrayList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    var popularityResponse: MutableLiveData<Boolean> = MutableLiveData(false)
    var topRatedResponse: MutableLiveData<Boolean> = MutableLiveData(false)
    private var scope = CoroutineScope(Dispatchers.Main)
    private var databaseScope = CoroutineScope(Dispatchers.IO)
    private var mDb: AppDatabase
    var favouriteMoviesList: LiveData<List<Movie>>
    var listOfImagesByteArray: LiveData<List<ByteArray>>
    var listOfDrawables: ArrayList<Drawable> = arrayListOf()
    private lateinit var getImagesJob: Deferred<Unit>

    // Scrolling Variables
    var popularityCount = 10
    var topRatedCount = 10
    var popularityPage = 3
    var topRatedPage = 3
    var isPopularity = true
    var isTopRated = true
    var loading = true


    init {
        mDb = getInstance(getApplication())!!
        favouriteMoviesList = mDb.movieDao.getAllFavouriteMovies()
        listOfImagesByteArray = mDb.movieDao.getAllFavouriteMoviesImageByteArray()
    }

    fun launchPopularityRetrofit(pageNumber: String = 1.toString()) {
        val retrofitService: RetrofitService = RetrofitInstance.retrofitInstance!!.create(
            RetrofitService::class.java
        )
        val moviesCall: Call<Movies> = retrofitService.getMoviesByPopularity(pageNumber)

        moviesCall.enqueue(object : Callback<Movies> {

            override fun onFailure(call: Call<Movies>, t: Throwable) {
                Log.i(Constants.MAIN_ACTIVITY_LOG_TAG, "failure popularity for " + t.message)
                popularityResponse.value = false
            }

            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                val moviesAll: Movies? = response.body()
                val movies: ArrayList<Movie> = moviesAll!!.movies
                val paths: ArrayList<String> = arrayListOf()
                for (movie in movies) {
                    if (movie.posterPath == null) {
                        movie.posterPath = ""
                    }
                    paths.add(movie.posterPath)
                    moviesPopularityArrayList.add(movie)
                }

                if (pathsPopularityArrayList.value != null) {
                    val pat = pathsPopularityArrayList.value!!
                    for (path in paths) {
                        pat.add(path)
                    }
                    pathsPopularityArrayList.value = pat
                } else pathsPopularityArrayList.value = paths

                Log.i(
                    Constants.MAIN_ACTIVITY_LOG_TAG,
                    "true Retro Popularity paths Size = " + paths.size.toString()
                )

            }

        })
    }

    fun launchTopRatedRetrofit(pageNumber: String = 1.toString()) {
        val retrofitService: RetrofitService = RetrofitInstance.retrofitInstance!!.create(
            RetrofitService::class.java
        )
        val moviesCall: Call<Movies> = retrofitService.getMoviesByTopRated(pageNumber)

        moviesCall.enqueue(object : Callback<Movies> {

            override fun onFailure(call: Call<Movies>, t: Throwable) {
                Log.i(Constants.MAIN_ACTIVITY_LOG_TAG, "failure top rated for " + t.message)
                topRatedResponse.value = false
            }

            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                val moviesAll: Movies? = response.body()
                val movies: ArrayList<Movie> = moviesAll!!.movies
                val paths: ArrayList<String> = arrayListOf()
                for (movie in movies) {
                    if (movie.posterPath == null) {
                        movie.posterPath = ""
                    }
                    paths.add(movie.posterPath)
                    moviesTopRatedArrayList.add(movie)
                }

                if (pathsTopRatedArrayList.value != null) {
                    val pat = pathsTopRatedArrayList.value!!
                    for (path in paths) {
                        pat.add(path)
                    }
                    pathsTopRatedArrayList.value = pat
                } else pathsTopRatedArrayList.value = paths

                Log.i(
                    Constants.MAIN_ACTIVITY_LOG_TAG,
                    "true Retro TopRated paths size = " + paths.size.toString()
                )
            }
        })
    }

    fun checkPopularityNetwork() {
        scope.async {
            if (!NetworkChecker.isNetworkConnected(getApplication())) {

                Log.i(Constants.MAIN_ACTIVITY_LOG_TAG, "Check Popularity Network = false Check")
                popularityResponse.value = false
            } else {
                Log.i(Constants.MAIN_ACTIVITY_LOG_TAG, "Check Popularity Network = true Check")
                popularityResponse.value = true
            }
        }
    }

    fun checkTopRatedNetwork() {
        scope.async {
            if (!NetworkChecker.isNetworkConnected(getApplication())) {

                Log.i(Constants.MAIN_ACTIVITY_LOG_TAG, "Check Top Rated Network = false Check")
                topRatedResponse.value = false
            } else {

                Log.i(Constants.MAIN_ACTIVITY_LOG_TAG, "Check Top Rated Network = true Check")
                topRatedResponse.value = true
            }
        }

    }

    suspend fun getImagesByteArray() {
        getImagesJob = databaseScope.async {
            if (listOfDrawables.size > listOfImagesByteArray.value!!.size) {
                listOfDrawables = arrayListOf()
            }
            for (i in listOfImagesByteArray.value!!.indices) {
                if (i + 1 <= listOfDrawables.size)
                    continue
                val imageByteArray = listOfImagesByteArray.value!![i]
                val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
                listOfDrawables.add(
                    BitmapDrawable(
                        getApplication<Application>().resources,
                        bitmap
                    )
                )
            }
        }
        print(getImagesJob.await().toString())
    }
}