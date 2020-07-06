package com.example.popularmovies.screens.MainActivity

import com.example.popularmovies.models.movies.Movie

class MainPresenter(private val mainContractView: MainContract.View) : MainContract.Presenter {

    override fun onLaunchResponseObserver() {
        mainContractView.showProgressBar()
        mainContractView.launchResponseObservers()
    }

    override fun fireHideProgressBar() {
        mainContractView.hideProgressBar()
    }

    override fun onLaunchPopularityObserver() {
        mainContractView.launchPopularityObserver()
    }

    override fun onLaunchTopRatedObserver() {
        mainContractView.launchTopRatedObserver()
    }

    override fun onLaunchPopularityRetrofit(pageNumber: String) {
        mainContractView.showProgressBar()
        mainContractView.launchPopularityRetrofit(pageNumber)
    }

    override fun onLaunchTopRatedRetrofit(pageNumber: String) {
        mainContractView.showProgressBar()
        mainContractView.launchTopRatedRetrofit(pageNumber)
    }

    override fun onLoadNextActivity(movie: Movie) {
        mainContractView.navigateToNextActivity(movie)
    }

    override fun onLaunchFavouriteObserver() {
        mainContractView.showProgressBar()
        mainContractView.launchFavouriteObserver()
    }
}