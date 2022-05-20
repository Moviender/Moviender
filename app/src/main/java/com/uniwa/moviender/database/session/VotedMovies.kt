package com.uniwa.moviender.database.session

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Movie::class,
            parentColumns = ["movielens_id"],
            childColumns = ["movielens_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class VotedMovies(
    @PrimaryKey @ColumnInfo(name = "movielens_id")
    val movielensId: String,
    val liked: Boolean
)