package com.uniwa.moviender.database.session

import androidx.paging.PagingSource
import androidx.room.*
import com.uniwa.moviender.database.session.crossref.SessionMovieCrossRef

@Dao
interface SessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: Session)

    // A movie can be shared with many sessions
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSessionMovieCrossRef(crossRefs: List<SessionMovieCrossRef>)

    @Query("SELECT * FROM Movies " +
            "JOIN SessionMovieCrossRef ON Movies.movielens_id = SessionMovieCrossRef.movielens_id " +
            "JOIN Sessions ON SessionMovieCrossRef.session_id = Sessions.session_id " +
            "WHERE Sessions.session_id = :sessionId")
    fun getSessionMovies(sessionId: String): PagingSource<Int, Movie>

    // By deleting the session record the deletion will be cascaded
    // and as result all movies vote of this session will be deleted as
    // well (otherwise a complex query would be needed). Remote keys of this
    // session will be deleted as well
    @Transaction
    @Query("DELETE FROM Sessions where session_id = :sessionId ")
    suspend fun deleteSession(sessionId: String)
}