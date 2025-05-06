package com.example.todo.model.service.impl

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class UserPreferencesRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {
    private companion object {
        val THEME_KEY = booleanPreferencesKey("dark_theme_enabled")
        const val TAG = "UserPreferencesRepo"
    }

    suspend fun saveLayoutPreference(isDarkThemeEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkThemeEnabled
        }
    }

    val isDarkThemeEnabled: Flow<Boolean> = dataStore.data.catch {
        if(it is IOException){
            Log.e(TAG, "Error reading preferences.", it)
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map {
        preferences -> preferences[THEME_KEY] ?: true
    }
}