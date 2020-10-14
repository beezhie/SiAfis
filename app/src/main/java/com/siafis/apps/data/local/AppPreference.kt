package com.siafis.apps.data.local

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreference(
    context: Context
) {
    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences>

    init {
        dataStore = applicationContext.createDataStore(
            name = "app_preferences"
        )
    }

    val intro: Flow<Boolean?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_INTRO]
        }

    suspend fun saveIntro(login: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_INTRO] = login
        }
    }

    companion object {
        val KEY_INTRO = preferencesKey<Boolean>("key_intro")
    }
}