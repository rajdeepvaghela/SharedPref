package com.rdapps.sharedpref.pref

import android.content.Context
import android.content.SharedPreferences
import com.rdapps.sharedpref.App
import com.rdapps.sharedpref.BuildConfig
import com.rdapps.sharedpref.utils.Base64Util
import java.util.*

/**
 * Created by Rajdeep Vaghela on 21/11/20
 */

enum class Pref(val defaultValue: Any) {
    USER_NAME(""),
    USER_AGE(20),
    LOGGED_IN_TIME(0L),
    IS_LOGGED_IN(false);

    private val prefName = BuildConfig.APPLICATION_ID
    val sharedPref: SharedPreferences =
        App.context().getSharedPreferences(prefName, Context.MODE_PRIVATE)

    fun getKey(secure: Boolean): String {
        val key = name.toLowerCase(Locale.ROOT)
        return if (secure) Base64Util.stringToBase64(key) else key
    }

    fun setValue(value: Any, secure: Boolean = false) {

        when (value) {
            is String -> {
                sharedPref.edit().putString(getKey(secure), value).apply()
            }
            is Boolean -> {
                sharedPref.edit().putBoolean(getKey(secure), value).apply()
            }
            is Float -> {
                sharedPref.edit().putFloat(getKey(secure), value).apply()
            }
            is Long -> {
                sharedPref.edit().putLong(getKey(secure), value).apply()
            }
            is Int -> {
                sharedPref.edit().putInt(getKey(secure), value).apply()
            }
        }
    }

    inline fun <reified T> getValue(default: T? = null): T {
        val secure = !sharedPref.contains(getKey(false))

        return when (val defaultValue = default ?: this.defaultValue) {
            is String -> {
                sharedPref.getString(getKey(secure), defaultValue) as T
            }
            is Boolean -> {
                return sharedPref.getBoolean(getKey(secure), defaultValue) as T
            }
            is Float -> {
                return sharedPref.getFloat(getKey(secure), defaultValue) as T
            }
            is Long -> {
                return sharedPref.getLong(getKey(secure), defaultValue) as T
            }
            is Int -> {
                return sharedPref.getInt(getKey(secure), defaultValue) as T
            }
            else -> throw RuntimeException("Return type not supported")
        }
    }
}