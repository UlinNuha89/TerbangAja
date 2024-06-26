package com.andc4.terbangaja.data.source.local.pref

import android.content.SharedPreferences
import com.andc4.terbangaja.utils.SharedPreferenceUtils.set

interface AuthPreference {
    fun getTokenOtp(): String?

    fun setTokenOtp(token: String)

    fun deleteTokenOtp()

    fun getToken(): String?

    fun setToken(token: String)

    fun getEmail(): String?

    fun setEmail(email: String)

    fun getPass(): String?

    fun setPass(pass: String)

    fun getUserID(): String?

    fun setUserID(userID: String)

    fun deleteAuth()
}

class AuthPreferenceImpl(private val pref: SharedPreferences) : AuthPreference {
    override fun getTokenOtp(): String? = pref.getString(KEY_TOKEN_OTP, null)

    override fun setTokenOtp(token: String) {
        pref[KEY_TOKEN_OTP] = token
    }

    override fun deleteTokenOtp() {
        pref[KEY_TOKEN_OTP] = null
    }

    override fun getToken(): String? = pref.getString(KEY_TOKEN, null)

    override fun setToken(token: String) {
        pref[KEY_TOKEN] = token
    }

    override fun getUserID(): String? = pref.getString(KEY_USER_ID, null)

    override fun setUserID(userID: String) { // Implementasikan metode setUserID
        pref[KEY_USER_ID] = userID
    }

    override fun getEmail(): String? = pref.getString(KEY_EMAIL, null)

    override fun setEmail(email: String) {
        pref[KEY_EMAIL] = email
    }

    override fun getPass(): String? = pref.getString(KEY_PASS, null)

    override fun setPass(pass: String) {
        pref[KEY_PASS] = pass
    }

    override fun deleteAuth() {
        pref[KEY_TOKEN] = null
        pref[KEY_EMAIL] = null
        pref[KEY_PASS] = null
    }

    companion object {
        const val PREF_NAME = "TerbangAja-pref"
        const val KEY_TOKEN_OTP = "KEY_TOKEN_OTP"
        const val KEY_EMAIL = "KEY_EMAIL"
        const val KEY_PASS = "KEY_PASS"
        const val KEY_TOKEN = "KEY_TOKEN"
        const val KEY_USER_ID = "KEY_USER_ID"
    }
}
