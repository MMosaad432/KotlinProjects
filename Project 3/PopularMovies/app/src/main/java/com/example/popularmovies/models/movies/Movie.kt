package com.example.popularmovies.models.movies


import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "favouriteMovies")
data class Movie
    (
    @SerializedName("id") @PrimaryKey(autoGenerate = false) var movieId: Int,
    @SerializedName("poster_path") @Ignore var posterPath: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) var imageByteArray : ByteArray?,
    @SerializedName("original_language") var originalLanguage: String,
    @SerializedName("original_title") var  originalTitle: String,
    @SerializedName("vote_average") var voteAverage: Double,
    @SerializedName("overview") var overview: String,
    @SerializedName("release_date") var releaseDate: String,
    var dateInserted: Date?
) : Parcelable {

    constructor() : this(545454,"posterSt",null,"enttt","MyOriginalTitletttttt",21212.2,"That is My overtttttt","1/4/2020ttttt",null)

    constructor(source: Parcel) : this(
        source.readInt(),
        source.readString()!!,
        source.createByteArray(),
        source.readString()!!,
        source.readString()!!,
        source.readDouble(),
        source.readString()!!,
        source.readString()!!,
        source.readSerializable() as Date?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(movieId)
        writeString(posterPath)
        writeByteArray(imageByteArray)
        writeString(originalLanguage)
        writeString(originalTitle)
        writeDouble(voteAverage)
        writeString(overview)
        writeString(releaseDate)
        writeSerializable(dateInserted)

    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Movie> = object : Parcelable.Creator<Movie> {
            override fun createFromParcel(source: Parcel): Movie =
                Movie(source)
            override fun newArray(size: Int): Array<Movie?> = arrayOfNulls(size)
        }
    }
}