package com.uniwa.moviender.database.session

import androidx.room.*
import com.squareup.moshi.Json
import com.uniwa.moviender.network.Movie

@Entity(tableName = "Movies")
data class Movie(
    @PrimaryKey @ColumnInfo(name = "movielens_id") @Json(name = "movielens_id")
    val movielensId: String,
    @ColumnInfo(name = "poster_path") @Json(name = "poster_path")
    val posterPath: String?,
    @Ignore
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
) {
    // To solve problem with Room related to the ignored field (didn't match
    // a constructor
    constructor(
        movielensId: String,
        posterPath: String,
        title: String,
        overview: String,
        releaseDate: String,
        voteAverage: Double
    ) : this(
        movielensId,
        posterPath,
        listOf<Int>(),
        title,
        overview,
        releaseDate,
        voteAverage
    )
}