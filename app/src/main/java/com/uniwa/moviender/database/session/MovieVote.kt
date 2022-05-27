package com.uniwa.moviender.database.session

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "movies_votes",
    primaryKeys = ["session_id", "movielens_id"],
    foreignKeys = [
        ForeignKey(
            entity = Session::class,
            parentColumns = ["session_id"],
            childColumns = ["session_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Movie::class,
            parentColumns = ["movielens_id"],
            childColumns = ["movielens_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MovieVote(
    @ColumnInfo(name = "session_id")
    val sessionId: String,
    @ColumnInfo(name = "movielens_id")
    val movielensId: String,
    val liked: Boolean
)