package com.example.popularmovies.screens.MainActivity

import com.example.popularmovies.models.movies.Movie

interface MainContract {

    interface Presenter {
        fun onLaunchResponseObserver()
        fun fireHideProgressBar()
        fun onLaunchPopularityRetrofit(pageNumber: String=1.toString())
        fun onLaunchTopRatedRetrofit(pageNumber: String = 1.toString())
        fun onLaunchPopularityObserver()
        fun onLaunchTopRatedObserver()
        fun onLoadNextActivity(movie: Movie)
        fun onLaunchFavouriteObserver()
    }

    interface View {
        fun showProgressBar()
        fun launchResponseObservers()
        fun launchPopularityRetrofit(pageNumber: String)
        fun launchTopRatedRetrofit(pageNumber: String)
        fun hideProgressBar()
        fun launchPopularityObserver()
        fun launchTopRatedObserver()
        fun launchFavouriteObserver()
        fun navigateToNextActivity(movie: Movie)
    }
}