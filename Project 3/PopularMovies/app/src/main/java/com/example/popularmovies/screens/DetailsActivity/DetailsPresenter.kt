package com.example.popularmovies.screens.DetailsActivity

import com.example.popularmovies.models.movies.Movie

class DetailsPresenter (private var detailsContractView: DetailsContract.View) :  DetailsContract.Presenter{
    override fun onLoadData(movie: Movie) {
        detailsContractView.showProgressBar()
        detailsContractView.loadMovieImage(posterPath = movie.posterPath)
        detailsContractView.populateUi(movie)

    }

    override fun fireProgressBarHiding() {
        detailsContractView.hideProgressBar()
    }

    override fun onLaunchResponseObserver(movieId: Int) {
        detailsContractView.launchResponseObserver(movieId)
    }

    override fun onLoadReviewsRetrofit(movieId: Int) {
        detailsContractView.loadReviewsRetrofit(movieId)
    }



    override fun onLoadTrailersRetrofit(movieId: Int) {
        detailsContractView.loadTrailersRetrofit(movieId)
    }

    override fun onUpdateReviewsRecyclerViewList() {
        detailsContractView.updateReviewsRecyclerViewList()
    }

    override fun onUpdateTrailersRecyclerViewList() {
        detailsContractView.updateTrailersRecyclerViewList()
    }


}