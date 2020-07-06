package com.example.popularmovies.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.popularmovies.models.movies.Movie

@Database(entities = [Movie::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val movieDao: MovieDao
}

private val LOCK = AppDatabase::class.java
private val DATABASE_NAME = "popularMoviesDatabase"
private var sInstance: AppDatabase? = null
fun getInstance(context: Context): AppDatabase? {
    if (sInstance == null) {
        synchronized(LOCK) {
            sInstance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, DATABASE_NAME
            ).build()
        }

    }

    return sInstance
}
