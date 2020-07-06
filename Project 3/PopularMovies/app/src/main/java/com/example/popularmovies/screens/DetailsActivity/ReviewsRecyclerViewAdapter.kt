package com.example.popularmovies.screens.DetailsActivity

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.popularmovies.databinding.ReviewsListBinding
import com.example.popularmovies.models.movieReviews.Review

class ReviewsRecyclerViewAdapter(
    private val context: Context,
    var reviewsArrayList: ArrayList<Review>,
    var connectedToNetwork : Boolean
) :
    RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ReviewsRecyclerViewAdapterViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewsRecyclerViewAdapterViewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(context)
        val reviewsListBinding = ReviewsListBinding.inflate(layoutInflater,parent,false)
        return ReviewsRecyclerViewAdapterViewHolder(reviewsListBinding)
    }

    override fun getItemCount(): Int {
        if (reviewsArrayList.size == 0){
            return 1
        }
        return reviewsArrayList.size
    }

    override fun onBindViewHolder(holder: ReviewsRecyclerViewAdapterViewHolder, position: Int) {
        if (reviewsArrayList.size != 0)
        holder.setData(reviewsArrayList[position])
        else holder.setData(null)
    }


    inner class ReviewsRecyclerViewAdapterViewHolder(private var reviewsListBinding: ReviewsListBinding) :
        RecyclerView.ViewHolder(reviewsListBinding.root){


        fun setData(review : Review?) {
            if (reviewsArrayList.size == 0){
                reviewsListBinding.reviewWriterTextView.visibility = View.GONE
                if (connectedToNetwork)
                    reviewsListBinding.contentTextView.text = "No reviews available for this film"
                else
                    reviewsListBinding.contentTextView.text = "No internet connection"
                reviewsListBinding.contentTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
                reviewsListBinding.contentTextView.setTypeface(reviewsListBinding.contentTextView.typeface, Typeface.BOLD)
                reviewsListBinding.contentTextView.textSize = 20f
                return
            }
            reviewsListBinding.reviewWriterTextView.text = review!!.reviewWriter.trim()
            reviewsListBinding.contentTextView.text = review.content
        }


    }


}