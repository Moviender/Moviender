package com.uniwa.moviender.database.session

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "MoviesGenres",
    foreignKeys = [
        ForeignKey(
            entity = Movie::class,
            parentColumns = ["movielens_id"],
            childColumns = ["movielens_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MovieGenre(
    @PrimaryKey @ColumnInfo(name = "movielens_id")
    val movielensId: String,
    @ColumnInfo(name = "genre_id")
    val genreId: Int
)