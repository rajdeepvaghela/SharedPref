package com.rdapps.sharedpref.pref

import android.content.Context
import androidx.core.content.edit
import com.rdapps.sharedpref.BuildConfig
import com.rdapps.sharedpref.utils.Base64Util

/**
 * Created by Rajdeep Vaghela on 21/11/20
 */

enum class Pref(val def: Any) {
    UserName(""),
    UserAge(0),
    LoggedInTime(0L),
    IsLoggedIn(false);

    fun getKey(isSecure: Boolean): String {
        val key = name.lowercase()
        return if (isSecure) Base64Util.stringToBase64(key) else key
    }

    context (Context) inline fun <reified T> set(value: T, isSecure: Boolean = false) {
        val pref = getSharedPreferences(BuildConfig.PREF_NAME, Context.MODE_PRIVATE)
        pref.edit {
            when (value) {
                is String -> putString(getKey(isSecure), value)
                is Boolean -> putBoolean(getKey(isSecure), value)
                is Float -> putFloat(getKey(isSecure), value)
                is Long -> putLong(getKey(isSecure), value)
                is Int -> putInt(getKey(isSecure), value)
            }
        }
    }

    context (Context) inline fun <reified T> get(
        default: T? = null,
        isSecure: Boolean? = null,
        clazz: Class<T> = T::class.java
    ): T {
        val pref = getSharedPreferences(BuildConfig.PREF_NAME, Context.MODE_PRIVATE)
        val secure = isSecure ?: !pref.contains(getKey(false))
        val value = pref.all[getKey(secure)] ?: default ?: def
        return clazz.cast(value) ?: default ?: error("Cast Failure")
    }
}