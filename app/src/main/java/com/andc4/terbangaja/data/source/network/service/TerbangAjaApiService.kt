package com.andc4.terbangaja.data.source.network.service

import android.content.Context
import com.andc4.terbangaja.BuildConfig
import com.andc4.terbangaja.data.source.local.pref.AuthPreferenceImpl
import com.andc4.terbangaja.data.source.network.model.BaseResponse
import com.andc4.terbangaja.data.source.network.model.auth.otp.OtpData
import com.andc4.terbangaja.data.source.network.model.auth.otp.OtpRequestPayload
import com.andc4.terbangaja.data.source.network.model.auth.register.RegisterData
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.util.concurrent.TimeUnit

interface TerbangAjaApiService {
    /*
        @POST("login") // Replace "login" with the actual endpoint path
        fun login(
            @Body loginRequest: LoginRequest
        ): Call<ResponseBody>*/

    @POST("auth/resend-otp")
    suspend fun resendOTP(): Response<BaseResponse<OtpData>>

    @POST("auth/verify-otp")
    suspend fun verifyOTP(
        @Body payload: OtpRequestPayload,
    ): Response<BaseResponse<OtpData>>

    @Multipart
    @POST("auth/register")
    suspend fun register(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part image: MultipartBody.Part? = null,
        @Part("phone") phoneNumber: RequestBody,
    ): Response<BaseResponse<RegisterData>>

    companion object {
        @JvmStatic
        operator fun invoke(context: Context): TerbangAjaApiService {
            val sharedPreferences =
                context.getSharedPreferences(AuthPreferenceImpl.PREF_NAME, Context.MODE_PRIVATE)
            val authPreference = AuthPreferenceImpl(sharedPreferences)
            val okHttpClient =
                OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .addInterceptor(AuthInterceptor(authPreference))
                    .build()
            val retrofit =
                Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
            return retrofit.create(TerbangAjaApiService::class.java)
        }
    }
}