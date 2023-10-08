package com.example.newsapp.data.source.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class ArticlePreferences private constructor(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun saveLogin(email: String) {
        dataStore.edit { preferences ->
            preferences[LOGIN_KEY] = true
            preferences[ACCOUNT_EMAIL_KEY] = email
        }
    }

    suspend fun clearData() {
        dataStore.edit { preferences ->
            preferences.remove(LOGIN_KEY)
            preferences.remove(ACCOUNT_EMAIL_KEY)
        }
    }

    fun getLogin(): Flow<Map<String, Any>> {
        return dataStore.data.map { preferences ->
            mapOf(
                "isLogin" to (preferences[LOGIN_KEY] ?: false),
                "email" to (preferences[ACCOUNT_EMAIL_KEY] ?: "null")
            )
        }
    }

    companion object {
        private val LOGIN_KEY = booleanPreferencesKey("has_login")
        private val ACCOUNT_EMAIL_KEY = stringPreferencesKey("account_email")

        @Volatile
        private var instance: ArticlePreferences? = null

        fun getInstance(context: Context): ArticlePreferences {
            return instance ?: synchronized(this) {
                instance ?: ArticlePreferences(
                    PreferenceDataStoreFactory.create(produceFile = {
                        context.preferencesDataStoreFile("pref_general")
                    })
                ).also { instance = it }
            }
        }
    }
}