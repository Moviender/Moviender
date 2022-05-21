package com.uniwa.moviender

import android.app.Application
import com.uniwa.moviender.database.SessionDatabase

class MovienderApplication : Application() {
    val database: SessionDatabase by lazy { SessionDatabase.getInstance(this) }
}