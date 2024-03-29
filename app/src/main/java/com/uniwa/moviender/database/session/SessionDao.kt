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

    // Used ORDER BY because movies was show sorted by primary key,
    // which is string. This also solved flickering problem where
    // one card showed above other because of re-sorting after
    // new data arrived
    @Query(
        "SELECT * FROM Movies " +
                "JOIN SessionMovieCrossRef ON Movies.movielens_id = SessionMovieCrossRef.movielens_id " +
                "JOIN Sessions ON SessionMovieCrossRef.session_id = Sessions.session_id " +
                "WHERE Sessions.session_id = :sessionId ORDER BY order_index ASC " +
                "LIMIT (SELECT COUNT(movielens_id) FROM movies_votes WHERE session_id = :sessionId), ${Int.MAX_VALUE}"
    )
    fun getSessionMovies(sessionId: String): PagingSource<Int, Movie>

    // By deleting the session record the deletion will be cascaded
    // and as result all movies vote of this session will be deleted as
    // well (otherwise a complex query would be needed). Remote keys of this
    // session will be deleted as well
    @Transaction
    @Query("DELETE FROM Sessions where session_id = :sessionId ")
    suspend fun deleteSession(sessionId: String)

    @Query("SELECT COUNT(session_id) FROM SessionMovieCrossRef WHERE session_id = :sessionId")
    suspend fun getLastOrder(sessionId: String): Int

    @Query("INSERT INTO movies_votes(session_id, movielens_id, liked) VALUES (:sessionId, " +
            "(SELECT movielens_id FROM SessionMovieCrossRef WHERE order_index = (SELECT COUNT(movielens_id) FROM movies_votes WHERE session_id = :sessionId)), " +
            ":liked)")
    suspend fun insertVote(sessionId: String, liked: Boolean)

    @Query("SELECT liked FROM movies_votes " +
            "JOIN SessionMovieCrossRef ON movies_votes.movielens_id = SessionMovieCrossRef.movielens_id " +
            "WHERE movies_votes.session_id = :sessionId ORDER BY order_index ASC")
    suspend fun getVotes(sessionId: String): List<Boolean>

    @Query("SELECT * FROM Movies " +
            "JOIN SessionMovieCrossRef ON Movies.movielens_id = SessionMovieCrossRef.movielens_id " +
            "WHERE SessionMovieCrossRef.session_id = :sessionId " +
            "AND SessionMovieCrossRef.movielens_id IN (:results) " +
            "ORDER BY order_index ASC")
    suspend fun getResultMoviesDetails(sessionId: String, results: List<String>): List<Movie>
}