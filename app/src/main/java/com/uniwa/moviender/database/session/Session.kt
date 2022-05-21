package com.uniwa.moviender.database.session

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "Sessions"
)
data class Session(
    @PrimaryKey
    @ColumnInfo(name = "session_id")
    val sessionId: String
)