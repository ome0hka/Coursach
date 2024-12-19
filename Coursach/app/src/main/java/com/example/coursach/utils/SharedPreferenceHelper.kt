package com.example.coursach.utils

import android.content.Context

class SharedPreferenceHelper(private val context: Context) {
    companion object{
        private const val MY_PREF_KEY = "MY_PREF"
    }

    fun saveStringdata(key: String, data: String){
        val sharePreference = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE)
        sharePreference.edit().putString(key, data).apply()
    }

    fun getStringData(key: String) : String?{
        val sharedPreference = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE)
        return sharedPreference.getString(key, null)
    }

}