package com.example.popularmovies.screens.DetailsActivity

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.popularmovies.models.movieReviews.Review
import com.example.popularmovies.models.movieReviews.ReviewsResult
import com.example.popularmovies.models.movieTrailers.Trailer
import com.example.popularmovies.models.movieTrailers.TrailersResult
import com.example.popularmovies.network.RetrofitInstance
import com.example.popularmovies.network.RetrofitService
import com.example.popularmovies.utils.Constants
import com.wessam.library.NetworkChecker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsViewModel(application: Application) : AndroidViewModel(application) {

    var reviewsArrayList: MutableLiveData<ArrayList<Review>> = MutableLiveData()
    var trailersArrayList: MutableLiveData<ArrayList<Trailer>> = MutableLiveData()
    var reviewsResponse: MutableLiveData<Boolean> = MutableLiveData(false)
    var trailersResponse: MutableLiveData<Boolean> = MutableLiveData(false)
    private var viewModelJob = SupervisorJob()
    private var scope =  CoroutineScope(Dispatchers.Main + viewModelJob)

    fun launchReviewsRetrofit(movieId: Int) {
        val retrofitService: RetrofitService = RetrofitInstance.retrofitInstance!!.create(
            RetrofitService::class.java
        )
        val reviewsCall: Call<ReviewsResult> = retrofitService.getMovieReviews(movieId)
        reviewsCall.enqueue(object : Callback<ReviewsResult> {
            override fun onFailure(call: Call<ReviewsResult>, t: Throwable) {
                Log.i(Constants.MAIN_ACTIVITY_LOG_TAG, "Review Failure " + t.message)
                reviewsResponse.value = false
            }

            override fun onResponse(call: Call<ReviewsResult>, response: Response<ReviewsResult>) {

                val reviewsArrayList: ArrayList<Review> = response.body()!!.reviews
                this@DetailsViewModel.reviewsArrayList.value = reviewsArrayList
                Log.i(
                    Constants.MAIN_ACTIVITY_LOG_TAG,
                    "true review with review list = ${reviewsArrayList.size}"
                )
            }

        })

    }

    fun launchTrailersRetrofit(movieId: Int) {
        val retrofitService: RetrofitService = RetrofitInstance.retrofitInstance!!.create(
            RetrofitService::class.java
        )

        val trailersCall: Call<TrailersResult> = retrofitService.getMovieTrailers(movieId)
        trailersCall.enqueue(object : Callback<TrailersResult> {
            override fun onFailure(call: Call<TrailersResult>, t: Throwable) {
                Log.i(Constants.MAIN_ACTIVITY_LOG_TAG, "Trailer Failure" + t.message)
                trailersResponse.value = false
            }

            override fun onResponse(
                call: Call<TrailersResult>,
                response: Response<TrailersResult>
            ) {
                val trailersArrayList: ArrayList<Trailer> = response.body()!!.trailers

                this@DetailsViewModel.trailersArrayList.value = trailersArrayList
                Log.i(
                    Constants.MAIN_ACTIVITY_LOG_TAG,
                    "true review with review list = ${trailersArrayList.size}"
                )
            }

        })

    }

    fun checkReviewsNetwork() {

        scope.async {
            if (!NetworkChecker.isNetworkConnected(getApplication())){

                Log.i(Constants.DETAILS_ACTIVITY_LOG_TAG, "Check Review Network = false Check")
                reviewsResponse.value = false
            }else{

                Log.i(Constants.DETAILS_ACTIVITY_LOG_TAG, "Check Review Network = true Check")
                reviewsResponse.value = true
            }
        }
    }

    fun checkTrailersNetwork() {
        scope.async {
            if (!NetworkChecker.isNetworkConnected(getApplication())){

                Log.i(Constants.DETAILS_ACTIVITY_LOG_TAG, "Check Trailer Network = false Check")
                trailersResponse.value = false
            }else{

                Log.i(Constants.DETAILS_ACTIVITY_LOG_TAG, "Check Trailer Network = true Check")
                trailersResponse.value = true
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}