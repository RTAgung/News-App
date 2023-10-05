package com.example.newsapp.data.source.preferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit

class ArticlePreferences private constructor(context: Context) {

    private val sharedPref = context.getSharedPreferences("PREF_GENERAL", MODE_PRIVATE)

    suspend fun clearData() {
        sharedPref.edit().clear().apply()
    }

    suspend fun saveLogin(email: String) {
        sharedPref.edit {
            putBoolean(LOGIN_KEY, true)
            putString(EMAIL_KEY, email)
            apply()
        }
    }

    suspend fun getLogin(): Map<String, Any?> {
        val login = sharedPref.getBoolean(LOGIN_KEY, false)
        val email = sharedPref.getString(EMAIL_KEY, "null")
        return mapOf(
            "login" to login,
            "email" to email
        )
    }

    companion object {
        private const val LOGIN_KEY = "login_key"
        private const val EMAIL_KEY = "email_key"

        @Volatile
        private var instance: ArticlePreferences? = null

        fun getInstance(context: Context): ArticlePreferences {
            return instance ?: synchronized(this) {
                instance ?: ArticlePreferences(context).also { instance = it }
            }
        }
    }
}