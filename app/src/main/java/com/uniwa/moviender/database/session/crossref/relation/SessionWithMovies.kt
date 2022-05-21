package com.uniwa.moviender.database.session.crossref.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.uniwa.moviender.database.session.Movie
import com.uniwa.moviender.database.session.Session
import com.uniwa.moviender.database.session.crossref.SessionMovieCrossRef

data class SessionWithMovies(
    @Embedded val session: Session,
    @Relation(
        parentColumn = "session_id",
        entityColumn = "movielens_id",
        associateBy = Junction(SessionMovieCrossRef::class)
    )
    val movies: List<Movie>
)