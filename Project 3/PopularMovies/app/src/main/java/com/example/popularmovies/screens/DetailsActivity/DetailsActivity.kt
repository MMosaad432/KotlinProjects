package com.example.popularmovies.screens.DetailsActivity

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.popularmovies.R
import com.example.popularmovies.database.AppDatabase
import com.example.popularmovies.database.getInstance
import com.example.popularmovies.databinding.ActivityDetailsBinding
import com.example.popularmovies.models.movieReviews.Review
import com.example.popularmovies.models.movieTrailers.Trailer
import com.example.popularmovies.models.movies.Movie
import com.example.popularmovies.utils.Constants
import com.wessam.library.NetworkChecker
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList


class DetailsActivity : AppCompatActivity(), DetailsContract.View,
    TrailersRecyclerViewAdapter.TrailersRecyclerViewOnClickListener {

    private lateinit var detailsActivityBinding: ActivityDetailsBinding
    lateinit var detailsContractPresenter: DetailsContract.Presenter

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor

    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var reviewsRecyclerViewAdapter: ReviewsRecyclerViewAdapter
    private lateinit var trailersRecyclerViewAdapter: TrailersRecyclerViewAdapter
    private val scope = CoroutineScope(Dispatchers.IO)
    lateinit var movie: Movie
    private lateinit var mDb: AppDatabase
    private var dateDeleted: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailsActivityBinding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(detailsActivityBinding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        sharedPreferences = getPreferences(0)
        sharedPreferencesEditor = sharedPreferences.edit()

        initRecyclerViewsAndViewModel()
        mDb = getInstance(this)!!

        movie = intent.getParcelableExtra(Constants.MOVIE_OBJECT)
        detailsContractPresenter = DetailsPresenter(this)
        detailsContractPresenter.onLoadData(movie)
        if (notConnectedAndXXReviewsAndTrailersXX()) {
            reviewsRecyclerViewAdapter = ReviewsRecyclerViewAdapter(this, ArrayList(), false)
            detailsActivityBinding.reviewsRecyclerView.adapter = reviewsRecyclerViewAdapter
            trailersRecyclerViewAdapter =
                TrailersRecyclerViewAdapter(this, this, ArrayList(), false)
            detailsActivityBinding.trailersRecyclerView.adapter = trailersRecyclerViewAdapter
        }

        detailsContractPresenter.onLaunchResponseObserver(movie.movieId)
    }


    private fun initRecyclerViewsAndViewModel() {
        detailsActivityBinding.favouriteImageButton.setOnClickListener {

            if ((it as ImageButton).tag == Constants.FAVOURITE_BORDER_ICON_TAG) {
                it.setImageResource(R.drawable.ic_favorite_64dp)
                it.tag = Constants.FAVOURITE_ICON_TAG
                sharedPreferencesEditor.putString(
                    movie.movieId.toString(),
                    Constants.FAVOURITE_ICON_TAG
                ).apply()
                val date: Date = Date()

                movie.dateInserted = if (dateDeleted != null) dateDeleted else date

                scope.launch {
                    addToDatabase()
                }

            } else {
                it.setImageResource(R.drawable.ic_favorite_border_64dp)
                it.tag = Constants.FAVOURITE_BORDER_ICON_TAG
                sharedPreferencesEditor.remove(movie.movieId.toString()).apply()
                dateDeleted = movie.dateInserted
                scope.launch { deleteFromDatabase() }
            }
        }
        detailsViewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        val reviewsLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val trailersLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        detailsActivityBinding.reviewsRecyclerView.layoutManager = reviewsLayoutManager
        detailsActivityBinding.trailersRecyclerView.layoutManager = trailersLayoutManager


    }

    override fun showProgressBar() {
        detailsActivityBinding.moviePosterProgressBar.visibility = View.VISIBLE
    }

    override fun loadMovieImage(posterPath: String) {
        if (movie.posterPath == "posterSt") {
            Glide.with(this).load(byteArrayToImage(movie.imageByteArray!!)).into(detailsActivityBinding.moviePosterImageView)
            detailsContractPresenter.fireProgressBarHiding()

        } else {
            if (movie.posterPath == ""){
                Glide.with(this).load(R.drawable.download_error).into(detailsActivityBinding.moviePosterImageView)
                imageToBytArray(resources.getDrawable(R.drawable.download_error))
                detailsContractPresenter.fireProgressBarHiding()
                return
            }
            Glide.with(this)
                .load(Constants.IMAGE_URL + posterPath)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {

                        imageToBytArray(resources.getDrawable(R.drawable.download_error))
                        detailsActivityBinding.moviePosterImageView.setImageResource(R.drawable.download_error)
                        detailsContractPresenter.fireProgressBarHiding()
                    return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        imageToBytArray(resource!!)
                        detailsContractPresenter.fireProgressBarHiding()
                        return false
                    }
                })
                .into(detailsActivityBinding.moviePosterImageView)
        }
    }

    override fun populateUi(movie: Movie) {
        if (sharedPreferences.contains(movie.movieId.toString())) {
            detailsActivityBinding.favouriteImageButton.setImageResource(R.drawable.ic_favorite_64dp)
            detailsActivityBinding.favouriteImageButton.tag = Constants.FAVOURITE_ICON_TAG
        } else {
            detailsActivityBinding.favouriteImageButton.setImageResource(R.drawable.ic_favorite_border_64dp)
            detailsActivityBinding.favouriteImageButton.tag = Constants.FAVOURITE_BORDER_ICON_TAG
        }
        supportActionBar?.title = movie.originalTitle
        val local = Locale(movie.originalLanguage)
        originalLanguageTextView.text = local.displayLanguage
        voteAverageTextView.text = movie.voteAverage.toString() + "/10"
        releaseDateTextView.text = movie.releaseDate
        overviewTextView.text = movie.overview
    }


    override fun hideProgressBar() {
        detailsActivityBinding.moviePosterProgressBar.visibility = View.GONE
    }

    override fun launchResponseObserver(movieId: Int) {
        if (connectedAndXXReviewsAndTrailers() || notConnectedAndXXReviewsAndTrailersXX()) {

            detailsViewModel.reviewsResponse.observe(this, Observer<Boolean> {

                Log.i(Constants.DETAILS_ACTIVITY_LOG_TAG, "Reviews response with $it")
                if (!it) {

                    detailsViewModel.checkReviewsNetwork()
                    return@Observer
                } else {
                    detailsContractPresenter.onLoadReviewsRetrofit(movieId)
                }

            })

            detailsViewModel.trailersResponse.observe(this, Observer<Boolean> {

                Log.i(Constants.DETAILS_ACTIVITY_LOG_TAG, "Trailers response with $it")
                if (!it) {
                    detailsViewModel.checkTrailersNetwork()
                    return@Observer
                } else {
                    detailsContractPresenter.onLoadTrailersRetrofit(movieId)
                }
            })

        } else {
            detailsContractPresenter.onUpdateReviewsRecyclerViewList()
            detailsContractPresenter.onUpdateTrailersRecyclerViewList()
        }
    }

    override fun loadReviewsRetrofit(movieId: Int) {
        detailsViewModel.launchReviewsRetrofit(movieId)
        detailsContractPresenter.onUpdateReviewsRecyclerViewList()
    }

    override fun loadTrailersRetrofit(movieId: Int) {
        detailsViewModel.launchTrailersRetrofit(movieId)
        detailsContractPresenter.onUpdateTrailersRecyclerViewList()
    }

    override fun updateReviewsRecyclerViewList() {
        detailsViewModel.reviewsArrayList.observe(this, Observer<ArrayList<Review>> {
            Log.i(
                Constants.DETAILS_ACTIVITY_LOG_TAG,
                "from Reviews observer with path Size = ${it.size}"
            )
            reviewsRecyclerViewAdapter = ReviewsRecyclerViewAdapter(this, it, true)
            detailsActivityBinding.reviewsRecyclerView.adapter = reviewsRecyclerViewAdapter
        })
    }

    override fun updateTrailersRecyclerViewList() {
        detailsViewModel.trailersArrayList.observe(this, Observer<ArrayList<Trailer>> {
            Log.i(
                Constants.DETAILS_ACTIVITY_LOG_TAG,
                "from Trailers observer with path Size = ${it.size}"
            )

            trailersRecyclerViewAdapter = TrailersRecyclerViewAdapter(this, this, it, true)

            detailsActivityBinding.trailersRecyclerView.adapter = trailersRecyclerViewAdapter

        })
    }

    override fun onTrailerClicked(key: String) {
        val webPage = Uri.parse(Constants.YOUTUBE_WATCH + key)
        val intent = Intent(Intent.ACTION_VIEW, webPage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun notConnectedAndXXReviewsAndTrailersXX(): Boolean {
        return !NetworkChecker.isNetworkConnected(this) && detailsViewModel.reviewsArrayList.value == null && detailsViewModel.trailersArrayList.value == null
    }

    private fun connectedAndXXReviewsAndTrailers(): Boolean {
        return NetworkChecker.isNetworkConnected(this) && detailsViewModel.reviewsArrayList.value == null && detailsViewModel.trailersArrayList.value == null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun byteArrayToImage(imageByteArray: ByteArray): Drawable {
        val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
        return BitmapDrawable(resources, bitmap)
    }

    private fun imageToBytArray(resource: Drawable) {
        scope.async {
            val bitmap = (resource as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val bitmapData: ByteArray = stream.toByteArray()
            movie.imageByteArray = bitmapData

        }

    }
    suspend fun addToDatabase(){
        mDb.movieDao.addMovie(movie)
    }
    suspend fun deleteFromDatabase(){
        mDb.movieDao.deleteMovie(movie)
    }
}
