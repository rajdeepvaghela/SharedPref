package com.rdapps.sharedpref.pref

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.rdapps.sharedpref.BuildConfig
import com.rdapps.sharedpref.utils.Base64Util
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map


enum class Store(val def: Any) {
    UserName(""),
    UserAge(0),
    LoggedInTime(0L),
    IsLoggedIn(false);

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getDataStoreKey(
        clazz: Class<T>,
        isSecure: Boolean = false
    ): Preferences.Key<T> {
        val key = name.lowercase().run {
            if (isSecure) Base64Util.stringToBase64(this) else this
        }

        return when (clazz::class.java) {
            Boolean::class.java -> booleanPreferencesKey(key)
            Float::class.java -> floatPreferencesKey(key)
            Long::class.java -> longPreferencesKey(key)
            Int::class.java -> intPreferencesKey(key)
            Double::class.java -> doublePreferencesKey(key)
            else -> stringPreferencesKey(key)
        } as Preferences.Key<T>
    }

    context (Context) suspend inline fun <reified T : Any> set(
        value: T,
        isSecure: Boolean = false
    ) {
        appDataStore.edit {
            it[getDataStoreKey(clazz = T::class.java, isSecure)] = value
        }
    }

    context (Context) inline fun <reified T : Any> getFlow(
        default: T? = null,
        isSecure: Boolean = false,
        clazz: Class<T> = T::class.java
    ): Flow<T> {
        return appDataStore.data
            .distinctUntilChangedBy { it[getDataStoreKey(clazz, isSecure)] }
            .map {
                clazz.cast(it[getDataStoreKey(clazz, isSecure)] ?: default ?: def)
                    ?: error("Cast Failure")
            }
    }
}

val Context.appDataStore by preferencesDataStore(name = BuildConfig.DATASTORE_NAME)