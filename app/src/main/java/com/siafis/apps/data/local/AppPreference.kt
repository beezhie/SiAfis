package com.siafis.apps.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.siafis.apps.utils.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreference(
    private val context: Context
) {

    val intro: Flow<Boolean?>
        get() = context.dataStore.data.map { preferences ->
            preferences[KEY_INTRO]
        }

    suspend fun saveIntro(login: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_INTRO] = login
        }
    }

    val tanggal: Flow<Boolean?>
        get() = context.dataStore.data.map { preferences ->
            preferences[KEY_GUIDE_DATE]
        }

    suspend fun saveTanggal(date: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_GUIDE_DATE] = date
        }
    }

    val atlet: Flow<Boolean?>
        get() = context.dataStore.data.map { preferences ->
            preferences[KEY_GUIDE_ALTET]
        }

    suspend fun saveAtlet(atlet: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_GUIDE_ALTET] = atlet
        }
    }

    companion object {
        val KEY_INTRO = booleanPreferencesKey("key_intro")
        val KEY_GUIDE_DATE = booleanPreferencesKey("key_guide_date")
        val KEY_GUIDE_ALTET = booleanPreferencesKey("key_guide_atlet")
    }
}