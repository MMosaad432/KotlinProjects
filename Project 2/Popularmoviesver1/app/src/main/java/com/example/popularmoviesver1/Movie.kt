package com.example.popularmoviesver1

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Movie
(
    @SerializedName("poster_path")    var posterPath: String,
    var original_language: String,
    var original_title: String,
    var vote_average: Double,
    var overview: String,
    var release_date: String
) : Parcelable {

    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readDouble()!!,
        source.readString()!!,
        source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(posterPath)
        writeString(original_language)
        writeString(original_title)
       writeDouble(vote_average)
        writeString(overview)
        writeString(release_date)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Movie> = object : Parcelable.Creator<Movie> {
            override fun createFromParcel(source: Parcel): Movie = Movie(source)
            override fun newArray(size: Int): Array<Movie?> = arrayOfNulls(size)
        }
    }
}