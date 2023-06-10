package com.uwange.coffeeapp.sharedpreference

import android.content.SharedPreferences
import com.google.gson.Gson
import com.uwange.coffeeapp.data.repositoryImpl.UserData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoffeePreference @Inject constructor(private val pref: SharedPreferences) {

    var userData: UserData?
        get() {
            return pref.getString("user_data", null)?.run {
                Gson().fromJson(this, UserData::class.java)
            }
        }
        set(value) { pref.edit().putString("user_data", Gson().toJson(value)).apply()}
}