package com.example.reoil.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "loginPrefs"
        private const val IS_LOGGED_IN = "isLoggedIn"
        private const val USERNAME = "username"
    }

    fun setLoginStatus(isLoggedIn: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    fun getLoginStatus(): Boolean {
        return preferences.getBoolean(IS_LOGGED_IN, false)
    }

    fun clearLoginStatus() {
        val editor = preferences.edit()
        editor.remove(IS_LOGGED_IN)
        editor.apply()
    }

    fun setUsername(username: String) {
        val editor = preferences.edit()
        editor.putString(USERNAME, username)
        editor.apply()
        }
    fun getUsername(): String? {
        return preferences.getString(USERNAME, null)
        }
    }
