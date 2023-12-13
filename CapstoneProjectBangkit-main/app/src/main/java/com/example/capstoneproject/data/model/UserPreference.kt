package com.example.capstoneproject.data.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
class UserPreference(private val dataStore: DataStore<Preferences>) {


    suspend fun getUser(): String {
        val preferences = dataStore.data.first() // Synchronously retrieve the preferences

        return preferences[USERNAME_KEY] ?: ""

    }
    suspend fun saveSession(username: String){
        dataStore.edit { preferences->
            preferences[USERNAME_KEY] = username
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
    fun getSession(): Flow<String>{
       return dataStore.data.map { pref->
           pref[USERNAME_KEY] ?: ""
       }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null
        private val USERNAME_KEY = stringPreferencesKey("username")
        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}