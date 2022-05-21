package com.uniwa.moviender.database.session.crossref

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.squareup.moshi.Json
import com.uniwa.moviender.database.session.Movie
import com.uniwa.moviender.database.session.Session

// Many to many relationship between Session nad Movie

@Entity(
    primaryKeys = ["session_id", "movielens_id"],
    foreignKeys = [
        ForeignKey(
            entity = Movie::class,
            parentColumns = ["movielens_id"],
            childColumns = ["movielens_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Session::class,
            parentColumns = ["session_id"],
            childColumns = ["session_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class SessionMovieCrossRef(
    @ColumnInfo(name = "session_id")
    val sessionId: String,
    @ColumnInfo(name = "movielens_id")
    val movielensId: String
)