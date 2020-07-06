package com.example.popularmovies.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.popularmovies.models.movies.Movie


@Dao
interface MovieDao {

    @Query("SELECT * FROM favouriteMovies ORDER BY dateInserted")
    fun getAllFavouriteMovies():LiveData<List<Movie>>

    @Query("SELECT imageByteArray FROM favouriteMovies ORDER BY dateInserted")
    fun getAllFavouriteMoviesImageByteArray():LiveData<List<ByteArray>>
    @Insert
    fun addMovie(movie: Movie)

    @Delete
    fun deleteMovie(movie: Movie)
}
