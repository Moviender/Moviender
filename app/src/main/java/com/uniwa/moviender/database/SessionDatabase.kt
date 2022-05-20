package com.uniwa.moviender.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.uniwa.moviender.database.session.*

@Database(
    entities = arrayOf(
        Session::class, Movie::class, MovieGenre::class, VotedMovies::class
    ),
    version = 1
)
@TypeConverters(Converters::class)
abstract class SessionDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao

    companion object {
        private var INSTANCE: SessionDatabase? = null

        fun getInstance(context: Context): SessionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    SessionDatabase::class.java,
                    "session_database"
                )
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}