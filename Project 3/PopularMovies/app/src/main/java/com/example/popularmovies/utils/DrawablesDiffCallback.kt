package com.example.popularmovies.utils

import android.graphics.drawable.Drawable
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import java.util.*

class DrawablesDiffCallback(oldDrawablesList: ArrayList<Drawable>,
                            newDrawablesList: ArrayList<Drawable>
) :
    DiffUtil.Callback() {
    private val mOldDrawablesList: ArrayList<Drawable> = oldDrawablesList
    private val mNewDrawablesList: ArrayList<Drawable> = newDrawablesList
    override fun getOldListSize(): Int {
        return mOldDrawablesList.size
    }

    override fun getNewListSize(): Int {
        return mNewDrawablesList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldDrawablesList[oldItemPosition] === mNewDrawablesList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldDrawable: Drawable = mOldDrawablesList[oldItemPosition]
        val newDrawable: Drawable = mNewDrawablesList[newItemPosition]
        return oldDrawable == newDrawable
    }

    @Nullable
    override fun getChangePayload(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Any? { // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }

}