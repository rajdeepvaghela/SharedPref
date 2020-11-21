package com.rdapps.sharedpref.utils

import android.util.Base64

/**
 * Created by Rajdeep Vaghela on 21/11/20
 */

object Base64Util {
    fun base64ToString(base64: String): String {
        return String(Base64.decode(base64, Base64.DEFAULT))
    }

    fun stringToBase64(string: String): String {
        return Base64.encodeToString(string.toByteArray(), Base64.DEFAULT)
    }
}