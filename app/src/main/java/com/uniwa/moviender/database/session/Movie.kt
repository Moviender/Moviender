package com.uniwa.moviender.database.session

import androidx.room.*
import com.squareup.moshi.Json

@Entity(tableName = "Movies")
data class Movie(
    @PrimaryKey @Json(name = "movielens_id")
    val movielensId: String,
    @ColumnInfo(name = "poster_path") @Json(name = "poster_path")
    val posterPath: String?,
    @Relation(
        parentColumn = "movielens_id",
        entityColumn = "genre_id"
    )
    @Json(name = "genre_ids")
    val genreIds: List<Int>,
    @ColumnInfo(name = "title") @Json(name = "title")
    val title: String,
    @ColumnInfo(name = "Overview") @Json(name = "overview")
    val overview: String,
    @ColumnInfo(name = "release_date") @Json(name = "release_date")
    val releaseDate: String,
    @ColumnInfo(name = "vote_average") @Json(name = "vote_average")
    val voteAverage: Double
)