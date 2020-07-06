package com.example.popularmovies.screens.MainActivity

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.popularmovies.R
import com.example.popularmovies.databinding.ActivityMainBinding
import com.example.popularmovies.models.movies.Movie
import com.example.popularmovies.screens.DetailsActivity.DetailsActivity
import com.example.popularmovies.utils.Constants
import com.example.popularmovies.utils.RecyclerViewScrollListener
import com.wessam.library.NetworkChecker
import com.wessam.library.NoInternetLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(),
    MainContract.View,
    MoviesRecyclerViewAdapter.MovieImageOnClickListener {
    private lateinit var mainContractPresenter: MainContract.Presenter
    private lateinit var activityMainBinding: ActivityMainBinding

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor

    private var networkWasNotHere = false
    private var sortedBy: Int = 1
    private lateinit var movieArrayList: ArrayList<Movie>
    private lateinit var mainViewModel: MainViewModel
    private lateinit var moviesRecyclerViewAdapter: MoviesRecyclerViewAdapter
    lateinit var scrollListener: RecyclerViewScrollListener
    lateinit var gridViewManager: GridLayoutManager
    lateinit var mySavedInstanceState: Bundle
    var isSavedPopularity: Boolean = false
    var isSavedTopRated: Boolean = false
    var isSavedFavourites: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        initRecyclerViewAndViewModel()

        sharedPreferences = getPreferences(0)
        sharedPreferencesEditor = sharedPreferences.edit()
        sortedBy = sharedPreferences.getInt(Constants.SORTED_BY, 1)

        if (savedInstanceState != null) {
            mySavedInstanceState = savedInstanceState
            when (sortedBy) {
                1 -> {
                    isSavedPopularity = true
                    isSavedTopRated = false
                    isSavedFavourites = false
                }
                2 -> {
                    isSavedPopularity = false
                    isSavedTopRated = true
                    isSavedFavourites = false
                }
                else -> {
                    isSavedPopularity = false
                    isSavedTopRated = false
                    isSavedFavourites = true
                }
            }
            scrollListener.popularityCount = mainViewModel.popularityCount
            scrollListener.topRatedCount = mainViewModel.topRatedCount
            scrollListener.popularityPage = mainViewModel.popularityPage
            scrollListener.topRatedPage = mainViewModel.topRatedPage
            scrollListener.isPopularity = mainViewModel.isPopularity
            scrollListener.loading = mainViewModel.loading
        }
        when (sortedBy) {
            1 -> supportActionBar?.title = "Popular Movies \t Popularity"
            2 -> supportActionBar?.title = "Popular Movies \t Top rated"
            else -> supportActionBar?.title = "Popular Movies \t Favourites"
        }
        if (notConnectedAndXXPopAndTopXX()) {
            NoInternetLayout.Builder(this, R.layout.activity_main)
            networkWasNotHere = true
        }
        mainContractPresenter = MainPresenter(this)
        mainContractPresenter.onLaunchResponseObserver()

        if (sortedBy == 3) {
            mainContractPresenter.onLaunchFavouriteObserver()
        }

    }

    private fun initRecyclerViewAndViewModel() {
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        gridViewManager =
            if (getScreenOrientation() == Configuration.ORIENTATION_PORTRAIT)
                GridLayoutManager(this, 2)
            else GridLayoutManager(this, 3)

        scrollListener = object : RecyclerViewScrollListener(gridViewManager) {
            override fun checkSortedBy() {
                when (sortedBy) {
                    1 -> {
                        scrollListener.isPopularity = true
                        scrollListener.isTopRated = false
                    }
                    2 -> {
                        scrollListener.isTopRated = true
                        scrollListener.isPopularity = false
                    }
                    else -> {
                        scrollListener.isPopularity = false
                        scrollListener.isTopRated = false
                    }
                }
            }

            override fun onLoadMore() {
                if (sortedBy == 1) {
                    mainContractPresenter.onLaunchPopularityRetrofit(scrollListener.popularityPage.toString())
                    scrollListener.popularityPage += 1
                } else if (sortedBy == 2) {
                    mainContractPresenter.onLaunchTopRatedRetrofit(scrollListener.topRatedPage.toString())
                    scrollListener.topRatedPage += 1
                }
            }
        }
        activityMainBinding.moviesRecyclerView.addOnScrollListener(scrollListener)
        activityMainBinding.moviesRecyclerView.layoutManager = gridViewManager
        activityMainBinding.moviesRecyclerView.itemAnimator = null
        moviesRecyclerViewAdapter =
            MoviesRecyclerViewAdapter(this, this, sortedBy = sortedBy)
        activityMainBinding.moviesRecyclerView.adapter = moviesRecyclerViewAdapter
    }

    override fun launchResponseObservers() {

        if (connectedAndXXPopAndTopXX() || notConnectedAndXXPopAndTopXX()) {

            mainViewModel.popularityResponse.observe(this, Observer<Boolean> {

                Log.i(Constants.MAIN_ACTIVITY_LOG_TAG, "Popularity response with $it")
                if (!it) {
                    mainViewModel.checkPopularityNetwork()
                    return@Observer
                } else {
                    mainContractPresenter.onLaunchPopularityRetrofit()

                    if (sortedBy == 1) {
                        if (networkWasNotHere)
                            this.setContentView(activityMainBinding.root)
                        mainContractPresenter.onLaunchPopularityObserver()
                    }
                }
            })

            mainViewModel.topRatedResponse.observe(this, Observer<Boolean> {
                Log.i(Constants.MAIN_ACTIVITY_LOG_TAG, "TopRated response with $it")
                if (!it) {
                    mainViewModel.checkTopRatedNetwork()
                    return@Observer
                } else {
                    mainViewModel.launchTopRatedRetrofit()
                    if (sortedBy == 2) {
                        if (networkWasNotHere) {
                            this.setContentView(activityMainBinding.root)
                        }
                        mainContractPresenter.onLaunchTopRatedObserver()
                    }
                }
            })

        } else {
            when (sortedBy) {
                1 -> mainContractPresenter.onLaunchPopularityObserver()
                2 -> mainContractPresenter.onLaunchTopRatedObserver()
                else -> mainContractPresenter.onLaunchFavouriteObserver()
            }
        }
    }

    private fun notConnectedAndXXPopAndTopXX(): Boolean {
        return !NetworkChecker.isNetworkConnected(this) && mainViewModel.pathsPopularityArrayList.value == null && mainViewModel.pathsTopRatedArrayList.value == null
    }

    private fun connectedAndXXPopAndTopXX(): Boolean {
        return NetworkChecker.isNetworkConnected(this) && mainViewModel.pathsPopularityArrayList.value == null && mainViewModel.pathsTopRatedArrayList.value == null
    }

    override fun showProgressBar() {
        activityMainBinding.progressBar.visibility = View.VISIBLE
    }

    override fun launchPopularityRetrofit(pageNumber: String) {
        mainViewModel.launchPopularityRetrofit(pageNumber)

        Handler().postDelayed({ mainViewModel.launchPopularityRetrofit(2.toString()) }, 1000)

    }

    override fun launchTopRatedRetrofit(pageNumber: String) {
        mainViewModel.launchTopRatedRetrofit(pageNumber)
        Handler().postDelayed(Runnable {
            mainViewModel.launchTopRatedRetrofit(2.toString())
        }, 1000)

    }

    override fun hideProgressBar() {
        activityMainBinding.progressBar.visibility = View.GONE
    }

    override fun launchPopularityObserver() {
        mainViewModel.pathsPopularityArrayList.observe(this, Observer<ArrayList<String>> {
            Log.i(
                Constants.MAIN_ACTIVITY_LOG_TAG,
                "from Popularity observer with path Size = ${it.size}"
            )
            goneToVisible()
            if (sortedBy != 1)
                activityMainBinding.moviesRecyclerView.layoutManager?.removeAllViews()
            moviesRecyclerViewAdapter.updateList(it as ArrayList<Any>, sortedBy = 1)
            mainContractPresenter.fireHideProgressBar()
            movieArrayList = mainViewModel.moviesPopularityArrayList
            if (isSavedPopularity) {
                Handler().postDelayed({
                    activityMainBinding.moviesRecyclerView.smoothScrollToPosition(
                        mySavedInstanceState.getInt(
                            Constants.SCROLL_TO_POSITION
                        )
                    )
                }, 1000)
            }
        })
    }

    override fun launchTopRatedObserver() {

        mainViewModel.pathsTopRatedArrayList.observe(this, Observer<ArrayList<String>> {
            Log.i(
                Constants.MAIN_ACTIVITY_LOG_TAG,
                "from TopRated observer with path size = ${it.size}"
            )
            goneToVisible()
            if (sortedBy != 2)
                activityMainBinding.moviesRecyclerView.layoutManager?.removeAllViews()
            moviesRecyclerViewAdapter.updateList(it as ArrayList<Any>, sortedBy = 2)
            mainContractPresenter.fireHideProgressBar()
            movieArrayList = mainViewModel.moviesTopRatedArrayList
            if (isSavedTopRated) {
                Handler().postDelayed({
                    activityMainBinding.moviesRecyclerView.smoothScrollToPosition(
                        mySavedInstanceState.getInt(
                            Constants.SCROLL_TO_POSITION
                        )
                    )
                }, 1000)
            }
        })
    }

    override fun launchFavouriteObserver() {
        if (mainViewModel.listOfImagesByteArray.hasActiveObservers())
            mainViewModel.listOfImagesByteArray.removeObservers(this)

        if (mainViewModel.favouriteMoviesList.hasActiveObservers())
            mainViewModel.favouriteMoviesList.removeObservers(this)

        mainViewModel.listOfImagesByteArray.observe(this, Observer {

            GlobalScope.launch(Dispatchers.Main) {
                if (mainViewModel.listOfDrawables.size != it.size)
                    mainViewModel.getImagesByteArray()

                if (sortedBy == 3) {
                    if (mainViewModel.listOfDrawables.size == 0) {
                        activityMainBinding.moviesRecyclerView.visibility = View.GONE
                        activityMainBinding.errorTextView.visibility = View.VISIBLE
                        activityMainBinding.errorTextView.text =
                            "You don't have films in your favourites.\nAdd Some!\n<3"
                    } else {

                        goneToVisible()
                        moviesRecyclerViewAdapter.updateList(
                            mainViewModel.listOfDrawables as ArrayList<Any>,
                            sortedBy = 3
                        )
                    }
                    mainContractPresenter.fireHideProgressBar()
                    if (isSavedFavourites) {
                        Handler().postDelayed({
                            activityMainBinding.moviesRecyclerView.smoothScrollToPosition(
                                mySavedInstanceState.getInt(Constants.SCROLL_TO_POSITION)
                            )
                        }, 1000)
                    }
                }
            }


        })
        mainViewModel.favouriteMoviesList.observe(this, Observer {
            if (sortedBy == 3)
                movieArrayList = it as ArrayList<Movie>
        })
    }

    private fun goneToVisible() {
        activityMainBinding.moviesRecyclerView.visibility = View.VISIBLE
        activityMainBinding.errorTextView.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.popularity && sortedBy != 1) {
            supportActionBar?.title = "Popular Movies \t Popularity"
            sortedBy = 1
            sharedPreferencesEditor.putInt(Constants.SORTED_BY, sortedBy)
            sharedPreferencesEditor.apply()

            if (mainViewModel.pathsPopularityArrayList.hasActiveObservers())
                mainViewModel.pathsPopularityArrayList.forceRefresh()
            else
                mainContractPresenter.onLaunchPopularityObserver()

            return true
        } else if (id == R.id.top_rated && sortedBy != 2) {
            supportActionBar?.title = "Popular Movies \t Top rated"
            sortedBy = 2
            sharedPreferencesEditor.putInt(Constants.SORTED_BY, sortedBy)
            sharedPreferencesEditor.apply()

            if (mainViewModel.pathsTopRatedArrayList.hasActiveObservers())
                mainViewModel.pathsTopRatedArrayList.forceRefresh()
            else
                mainContractPresenter.onLaunchTopRatedObserver()

            return true
        } else if (id == R.id.favourite && sortedBy != 3) {
            supportActionBar?.title = "Popular Movies \t Favourites"
            sortedBy = 3
            sharedPreferencesEditor.putInt(Constants.SORTED_BY, sortedBy)
            sharedPreferencesEditor.apply()

            mainContractPresenter.onLaunchFavouriteObserver()

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(Constants.SORTED_BY, sortedBy)
        outState.putInt(
            Constants.SCROLL_TO_POSITION,
            gridViewManager.findFirstVisibleItemPosition()
        )
    }

    private fun <T> MutableLiveData<T>.forceRefresh() {
        this.value = this.value
    }

    override fun navigateToNextActivity(movie: Movie) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(Constants.MOVIE_OBJECT, movie)
        startActivity(intent)
    }

    override fun onClicked(pos: Int) {
        mainContractPresenter.onLoadNextActivity(movieArrayList[pos])
    }

    private fun getScreenOrientation(): Int {
        val getOrient = windowManager.defaultDisplay
        var orientation: Int = Configuration.ORIENTATION_UNDEFINED
        orientation =
            if (getOrient.width < getOrient.height) {
                Configuration.ORIENTATION_PORTRAIT
            } else {
                Configuration.ORIENTATION_LANDSCAPE
            }

        return orientation
    }
}
