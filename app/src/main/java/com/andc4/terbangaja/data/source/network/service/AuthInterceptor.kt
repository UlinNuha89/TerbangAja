package com.andc4.terbangaja.data.source.network.service

import com.andc4.terbangaja.data.source.local.pref.AuthPreference
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val pref: AuthPreference) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        // Add the Authorization header if the token is not null
        val token =
            when {
                request.url.encodedPath.contains("auth/resend-otp") -> pref.getTokenOtp()
                request.url.encodedPath.contentEquals("auth") -> pref.getToken()
                else -> ""
            }
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}
