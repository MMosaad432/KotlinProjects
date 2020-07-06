package com.example.popularmoviesver1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_details.*
import java.util.*

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        var movie = intent.getParcelableExtra<Movie>(Constants.MOVIE_OBJECT)

        var local : Locale = Locale(movie.original_language)
        supportActionBar?.setTitle(movie.original_title)
        Glide.with(this).load(Constants.IMAGE_URL + movie.posterPath).into(moviePosterImageView)
        voteAverageTextView. text = movie.vote_average.toString() + "/10"
        originalLanguageTextView.text = local.displayLanguage
        releaseDateTextView.text = movie.release_date
        overviewTextView.text = movie.overview
        var f = ""
    }


}
