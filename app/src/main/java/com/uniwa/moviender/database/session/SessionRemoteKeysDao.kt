package com.uniwa.moviender.database.session

import androidx.room.*

@Dao
interface SessionRemoteKeysDao {
    // Replace so only one record for each session will be available
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSessionRemoteKey(keys: SessionRemoteKeys)

    @Query("SELECT * FROM remote_keys WHERE session_id = :sessionId")
    suspend fun getRemoteKeyBySessionId(sessionId: String): SessionRemoteKeys

    @Query("SELECT last_updated FROM remote_keys WHERE session_id = :sessionId")
    suspend fun getLastUpdated(sessionId: String): Long
}