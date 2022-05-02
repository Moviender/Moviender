package com.uniwa.moviender.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserInfo (
    @PrimaryKey val uid: String
)