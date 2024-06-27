package com.andc4.terbangaja.data.source.local.pref

import android.content.SharedPreferences
import com.andc4.terbangaja.utils.SharedPreferenceUtils.set

interface AuthPreference {
    fun getTokenOtp(): String?

    fun setTokenOtp(token: String)

    fun deleteTokenOtp()

    fun deleteToken()

    fun getToken(): String?

    fun setToken(token: String)
}

class AuthPreferenceImpl(private val pref: SharedPreferences) : AuthPreference {
    override fun getTokenOtp(): String? = pref.getString(KEY_TOKEN_OTP, null)

    override fun setTokenOtp(token: String) {
        pref[KEY_TOKEN_OTP] = token
    }

    override fun deleteTokenOtp() {
        pref[KEY_TOKEN_OTP] = null
    }

    override fun deleteToken() {
        pref[KEY_TOKEN] = null
    }

    override fun getToken(): String? = pref.getString(KEY_TOKEN, null)

    override fun setToken(token: String) {
        pref[KEY_TOKEN] = token
    }

    companion object {
        const val PREF_NAME = "TerbangAja-pref"
        const val KEY_TOKEN_OTP = "KEY_TOKEN_OTP"
        const val KEY_TOKEN = "KEY_TOKEN"
    }
}
