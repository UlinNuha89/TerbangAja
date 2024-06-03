package com.andc4.terbangaja.data.source.local.pref

import android.content.SharedPreferences
import com.andc4.terbangaja.utils.SharedPreferenceUtils.set

interface AuthPreference {
    fun getToken(): String?

    fun setToken(token: String)

    fun deleteToken()
}

class AuthPreferenceImpl(private val pref: SharedPreferences) : AuthPreference {
    override fun getToken(): String? = pref.getString(KEY_TOKEN_OTP, null)

    override fun setToken(token: String) {
        pref[KEY_TOKEN_OTP] = token
    }

    override fun deleteToken() {
        pref[KEY_TOKEN_OTP] = null
    }

    companion object {
        const val PREF_NAME = "TerbangAja-pref"
        const val KEY_TOKEN_OTP = "KEY_TOKEN_OTP"
    }
}
