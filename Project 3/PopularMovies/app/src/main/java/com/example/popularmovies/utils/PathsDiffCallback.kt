package com.example.popularmovies.utils

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import java.util.*

class PathsDiffCallback(
    oldPathsList: ArrayList<String>,
    newPathsList: ArrayList<String>
) :
    DiffUtil.Callback() {
    private val mOldPathsList: ArrayList<String> = oldPathsList
    private val mNewPathsList: ArrayList<String> = newPathsList
    override fun getOldListSize(): Int {
        return mOldPathsList.size
    }

    override fun getNewListSize(): Int {
        return mNewPathsList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        var  g :String = ""
        var f : String = ""
        try {
            g =  mOldPathsList[oldItemPosition]
            f= mNewPathsList[newItemPosition]
        }catch (ex : Exception){
            ex.message
        }
        return g === f
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPath: String = mOldPathsList[oldItemPosition]
        val newPath: String = mNewPathsList[newItemPosition]
        return oldPath == newPath
    }

    @Nullable
    override fun getChangePayload(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Any? { // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }

}