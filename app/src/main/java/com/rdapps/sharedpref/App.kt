package com.rdapps.sharedpref

import android.app.Application
import android.content.Context

/**
 * Created by Rajdeep Vaghela on 21/11/20
 */

class App : Application() {

    init {
        instance = this
    }

    companion object {
        private lateinit var instance: App

        fun context(): Context {
            return try {
                instance.applicationContext
            } catch (e: Exception) {
                App().applicationContext
            }
        }
    }
}