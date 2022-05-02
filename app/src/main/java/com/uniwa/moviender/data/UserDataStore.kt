package com.uniwa.moviender.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch
import java.io.IOException

private const val USER_UID_PREFERENCES_NAME = "user_uid_preferences"

private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(
    name = USER_UID_PREFERENCES_NAME
)

private val USER_UID = stringPreferencesKey("uid")

class UserDataStore(context: Context) {

    val preferenceFlow: Flow<String> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[USER_UID] ?: ""
        }

    suspend fun saveUserUid(uid: String, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[USER_UID] = uid
        }
    }
}
