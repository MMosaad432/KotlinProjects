package com.example.popularmovies.screens.DetailsActivity

import com.example.popularmovies.models.movies.Movie


interface DetailsContract {

    interface Presenter {
        fun onLoadData(movie : Movie)
        fun fireProgressBarHiding()
        fun onLaunchResponseObserver(movieId: Int)
        fun onLoadReviewsRetrofit(movieId : Int)
        fun onLoadTrailersRetrofit(movieId : Int)
        fun onUpdateReviewsRecyclerViewList()
        fun onUpdateTrailersRecyclerViewList()
    }
    interface View {
        fun loadMovieImage(posterPath : String)
        fun showProgressBar()
        fun populateUi(movie: Movie)
        fun hideProgressBar()
        fun launchResponseObserver(movieId: Int)
        fun loadReviewsRetrofit(movieId: Int)
        fun loadTrailersRetrofit(movieId: Int)
        fun updateReviewsRecyclerViewList()
        fun updateTrailersRecyclerViewList()
    }
}