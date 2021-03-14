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
    private val dataStore: DataStore<Preferences> = applicationContext.createDataStore(
        name = "app_preferences"
    )

    val intro: Flow<Boolean?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_INTRO]
        }

    suspend fun saveIntro(login: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_INTRO] = login
        }
    }

    val tanggal: Flow<Boolean?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_GUIDE_DATE]
        }

    suspend fun saveTanggal(date: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_GUIDE_DATE] = date
        }
    }

    val atlet: Flow<Boolean?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_GUIDE_ALTET]
        }

    suspend fun saveAtlet(atlet: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_GUIDE_ALTET] = atlet
        }
    }

    companion object {
        val KEY_INTRO = preferencesKey<Boolean>("key_intro")
        val KEY_GUIDE_DATE = preferencesKey<Boolean>("key_guide_date")
        val KEY_GUIDE_ALTET = preferencesKey<Boolean>("key_guide_atlet")
    }
}