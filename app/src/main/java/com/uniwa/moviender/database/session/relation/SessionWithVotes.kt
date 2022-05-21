package com.uniwa.moviender.database.session.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.uniwa.moviender.database.session.MovieVote
import com.uniwa.moviender.database.session.Session

data class SessionWithVotes(
    @Embedded val session: Session,
    @Relation(
        parentColumn = "session_id",
        entityColumn = "session_id"
    )
    val votes: List<MovieVote>
)