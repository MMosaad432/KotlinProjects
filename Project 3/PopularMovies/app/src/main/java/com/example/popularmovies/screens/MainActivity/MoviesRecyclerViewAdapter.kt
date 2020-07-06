package com.example.popularmovies.screens.MainActivity

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.popularmovies.R
import com.example.popularmovies.databinding.MovieListBinding
import com.example.popularmovies.utils.Constants
import com.example.popularmovies.utils.DrawablesDiffCallback
import com.example.popularmovies.utils.MyExecutor
import com.example.popularmovies.utils.PathsDiffCallback


class MoviesRecyclerViewAdapter(
    val context: Context,
    var movieImageOnClickListener: MovieImageOnClickListener,
    var pathsPopularityArrayList: ArrayList<String> = arrayListOf(),
    var pathsTopRatedArrayList: ArrayList<String> = arrayListOf(),
    var listOfDrawables: ArrayList<Drawable> = arrayListOf(),
    private var sortedBy: Int
) :
    RecyclerView.Adapter<MoviesRecyclerViewAdapter.MoviesRecyclerViewAdapterViewHolder>() {


    private val executor = MyExecutor()

    interface MovieImageOnClickListener {
        fun onClicked(pos: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MoviesRecyclerViewAdapterViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val notificationListBinding: MovieListBinding =
            MovieListBinding.inflate(layoutInflater, parent, false)
        return MoviesRecyclerViewAdapterViewHolder(notificationListBinding)
    }

    override fun getItemCount(): Int {
        return when (sortedBy) {
            3 -> {
                listOfDrawables.size
            }
            2 -> {
                pathsTopRatedArrayList.size
            }
            else -> pathsPopularityArrayList.size
        }
    }

    override fun onBindViewHolder(holder: MoviesRecyclerViewAdapterViewHolder, position: Int) {
        if (sortedBy == 3) {
            holder.setDataForFavourites(position)
        } else
            holder.setData(position)
    }

    fun updateList(
        pathsArrayList: ArrayList<Any>,
        sortedBy: Int
    ) {
        this.sortedBy = sortedBy
        when (sortedBy) {
            3 -> {
                executor.execute {
                    if (this.listOfDrawables.size == pathsArrayList.size)
                        this.listOfDrawables.clear()
                    val diffCallback =
                        DrawablesDiffCallback(this.listOfDrawables, pathsArrayList as ArrayList<Drawable>)
                    val diffResult = DiffUtil.calculateDiff(diffCallback)

                    this.listOfDrawables.clear()
                    this.listOfDrawables.addAll(pathsArrayList)
                    (context as MainActivity).runOnUiThread {
                        diffResult.dispatchUpdatesTo(this)

                    }
                }
            }
            2 -> {
                executor.execute {
                    if (this.pathsTopRatedArrayList.size == pathsArrayList.size)
                        this.pathsTopRatedArrayList.clear()
                    val diffCallback =
                        PathsDiffCallback(this.pathsTopRatedArrayList, pathsArrayList as ArrayList<String>)
                    val diffResult = DiffUtil.calculateDiff(diffCallback)

                    this.pathsTopRatedArrayList.clear()
                    this.pathsTopRatedArrayList.addAll(pathsArrayList)
                    (context as MainActivity).runOnUiThread {
                        diffResult.dispatchUpdatesTo(this)

                    }
                }

            }
            else -> {
                executor.execute {
                    if (this.pathsPopularityArrayList.size == pathsArrayList.size)
                        this.pathsPopularityArrayList.clear()
                    val diffCallback =
                        PathsDiffCallback(this.pathsPopularityArrayList, pathsArrayList as ArrayList<String>)
                    val diffResult = DiffUtil.calculateDiff(diffCallback)

                    this.pathsPopularityArrayList.clear()
                    this.pathsPopularityArrayList.addAll(pathsArrayList)
                    (context as MainActivity).runOnUiThread {
                        diffResult.dispatchUpdatesTo(this)
                    }
                }
            }
        }

    }


    inner class MoviesRecyclerViewAdapterViewHolder(private var movieListBinding: MovieListBinding) :
        RecyclerView.ViewHolder(movieListBinding.root), View.OnClickListener {

        private var positionSelected: Int = -1

        init {
            movieListBinding.root.setOnClickListener(this)
        }

        fun setData(pos: Int) {
            this.positionSelected = pos
            if (sortedBy == 2) {
                if (pathsTopRatedArrayList[pos] == "") {
                    Glide.with(context).load(R.drawable.download_error)
                        .into(movieListBinding.movieImageView)
                    return
                }
                Glide.with(context).load(Constants.IMAGE_URL + pathsTopRatedArrayList[pos])
                    .into(movieListBinding.movieImageView)
            }else{
                if (pathsPopularityArrayList[pos] == "") {
                    Glide.with(context).load(R.drawable.download_error)
                        .into(movieListBinding.movieImageView)
                    return
                }
                Glide.with(context).load(Constants.IMAGE_URL + pathsPopularityArrayList[pos])
                    .into(movieListBinding.movieImageView)
            }
        }

        fun setDataForFavourites(pos: Int) {
            this.positionSelected = pos
            Glide.with(context).load(listOfDrawables[pos]).into(movieListBinding.movieImageView)
        }

        override fun onClick(v: View?) {
            movieImageOnClickListener.onClicked(positionSelected)
        }

    }


}