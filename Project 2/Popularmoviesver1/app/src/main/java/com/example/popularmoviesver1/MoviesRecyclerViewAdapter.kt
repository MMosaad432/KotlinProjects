package com.example.popularmoviesver1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.movie_list.view.*

class MoviesRecyclerViewAdapter(val context: Context,movieImageOnClickListener:MovieImageOnClickListener,var pathsArrayList:ArrayList<String>) :
    RecyclerView.Adapter<MoviesRecyclerViewAdapter.MoviesRecyclerViewAdapterViewHolder>() {

    interface MovieImageOnClickListener {
        fun onClicked(pos:Int)
    }
    var  movieImageOnClickListener: MovieImageOnClickListener = movieImageOnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesRecyclerViewAdapterViewHolder {

        return MoviesRecyclerViewAdapterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.movie_list,parent,false))
    }

    override fun getItemCount(): Int {
        return pathsArrayList.size
    }

    override fun onBindViewHolder(holder: MoviesRecyclerViewAdapterViewHolder, position: Int) {
        holder.setData(position)

    }


    inner class MoviesRecyclerViewAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

        var positionSelected : Int = -1
        init {
            itemView.setOnClickListener(this)
        }

        fun setData(pos : Int){
            this.positionSelected = pos
            Glide.with(context).load(Constants.IMAGE_URL + pathsArrayList.get(pos)).into(itemView.movieImageView)
        }
        override fun onClick(v: View?) {
            movieImageOnClickListener.onClicked(positionSelected)
        }

    }


}