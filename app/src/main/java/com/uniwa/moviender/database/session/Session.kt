package com.uniwa.moviender.database.session

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "Sessions",
    primaryKeys = ["uid", "friend_uid"]
)
data class Session(
    @ColumnInfo(name = "uid") val uid: String,
    @ColumnInfo(name = "friend_uid") val friendUid: String
)