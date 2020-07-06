package com.example.popularmovies.utils


import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


abstract class RecyclerViewScrollListener protected constructor(private val mLayoutManager: GridLayoutManager) :
    RecyclerView.OnScrollListener() {

    var popularityCount = 10
    var topRatedCount = 10
    var popularityPage = 3
    var topRatedPage = 3
    var isPopularity = true
    var isTopRated = true
    var loading = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        checkSortedBy()
        if (popularityPage == 50 && isPopularity)
            return
        if (topRatedPage == 50 && isTopRated)
            return
        val totalItemCount = mLayoutManager.itemCount
        val lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition()
        if (loading && totalItemCount > popularityCount&& isPopularity) {
            loading = false
            popularityCount = totalItemCount
        }else if (loading && totalItemCount > topRatedCount && isTopRated) {
            loading = false
            topRatedCount = totalItemCount
        }
        val visibleThreshold = 2
        if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {

            onLoadMore()
            loading = true
        }
    }

    /*fun resetState() {
        page = 1
        count = 0
        loading = true
    }*/

    /*fun setState(page: Int, count: Int) {
        this.page = page
        this.count = count
        loading = false
    }*/



    abstract fun onLoadMore()
    abstract fun checkSortedBy()

}