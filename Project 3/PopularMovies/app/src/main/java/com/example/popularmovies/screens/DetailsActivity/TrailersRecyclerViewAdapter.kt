package com.example.popularmovies.screens.DetailsActivity

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.popularmovies.databinding.TrailersListBinding
import com.example.popularmovies.models.movieTrailers.Trailer
import com.example.popularmovies.utils.Constants

class TrailersRecyclerViewAdapter(
    val context: Context,
    var trailersRecyclerViewOnClickListener: TrailersRecyclerViewOnClickListener,
    var trailersArrayList: ArrayList<Trailer>,
    var connectedToNetwork : Boolean
) :
    RecyclerView.Adapter<TrailersRecyclerViewAdapter.TrailersRecyclerViewAdapterViewHolder>() {

    interface TrailersRecyclerViewOnClickListener {
        fun onTrailerClicked(key: String)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrailersRecyclerViewAdapterViewHolder {

        val layoutInflater : LayoutInflater = LayoutInflater.from(context)
        val trailersListBinding = TrailersListBinding.inflate(layoutInflater,parent,false)
        return TrailersRecyclerViewAdapterViewHolder(trailersListBinding)

    }

    override fun getItemCount(): Int {
        if (trailersArrayList.size == 0)
            return 1
        return trailersArrayList.size
    }

    override fun onBindViewHolder(holder: TrailersRecyclerViewAdapterViewHolder, position: Int) {
        if (trailersArrayList.size!=0)
        holder.setData(trailersArrayList[position])
        else holder.setData(null)
    }


    inner class TrailersRecyclerViewAdapterViewHolder(var trailersListBinding: TrailersListBinding) :
        RecyclerView.ViewHolder(trailersListBinding.root), View.OnClickListener {

        private lateinit var key: String

        init {
            itemView.setOnClickListener(this)
        }

        fun setData(trailer : Trailer?) {
            if (trailersArrayList.size== 0){
                trailersListBinding.playImageView.visibility = View.GONE
                trailersListBinding.trailerThumbnailImageView.visibility = View.GONE
                if (connectedToNetwork)
                trailersListBinding.trailerName.text = "No trailers available for this film"
                else
                    trailersListBinding.trailerName.text = "No internet connection"
                trailersListBinding.trailerName.textAlignment = View.TEXT_ALIGNMENT_CENTER
                trailersListBinding.trailerName.setTypeface(trailersListBinding.trailerName.typeface, Typeface.BOLD)
                trailersListBinding.trailerName.textSize = 20f
                return
            }
            this.key = trailer!!.thumbnailKey
            Glide.with(context)
                .load(Uri.parse(Constants.YOUTUBE_THUMBNAIL + key +Constants.THUMBNAIL_QUALITY))
                .listener(object :  RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        trailersListBinding.trailerName.width = resource?.intrinsicWidth!!
                        return false
                    }


                })
                .into(trailersListBinding.trailerThumbnailImageView)
            trailersListBinding.trailerName.text = trailer.trailerName
        }

        override fun onClick(v: View?) {
            if (trailersArrayList.size!=0)
            trailersRecyclerViewOnClickListener.onTrailerClicked(key)
        }

    }


}