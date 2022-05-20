package com.uniwa.moviender.database.session

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Sessions")
data class Session(
    @PrimaryKey val uid: String,
    @PrimaryKey val friendUid: String
)