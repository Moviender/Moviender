package com.uniwa.moviender.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.uniwa.moviender.database.session.*
import com.uniwa.moviender.database.session.crossref.SessionMovieCrossRef

@Database(
    entities = arrayOf(
        Session::class,
        Movie::class,
        MovieVote::class,
        SessionMovieCrossRef::class,
        SessionRemoteKeys::class
    ),
    version = 1
)
@TypeConverters(Converters::class)
abstract class SessionDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun remoteKeysDao(): SessionRemoteKeysDao

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