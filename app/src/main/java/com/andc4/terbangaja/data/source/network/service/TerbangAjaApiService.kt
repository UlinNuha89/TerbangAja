package com.andc4.terbangaja.data.source.network.service

import android.content.Context
import com.andc4.terbangaja.BuildConfig
import com.andc4.terbangaja.data.source.local.pref.AuthPreferenceImpl
import com.andc4.terbangaja.data.source.network.model.BasePaging
import com.andc4.terbangaja.data.source.network.model.BaseResponse
import com.andc4.terbangaja.data.source.network.model.auth.forgotpassword.ForgotPasswordRequest
import com.andc4.terbangaja.data.source.network.model.auth.forgotpassword.ForgotPasswordResponse
import com.andc4.terbangaja.data.source.network.model.auth.login.LoginData
import com.andc4.terbangaja.data.source.network.model.auth.login.LoginRequest
import com.andc4.terbangaja.data.source.network.model.auth.otp.OtpData
import com.andc4.terbangaja.data.source.network.model.auth.otp.OtpRequestPayload
import com.andc4.terbangaja.data.source.network.model.auth.profile.ProfileData
import com.andc4.terbangaja.data.source.network.model.auth.register.RegisterData
import com.andc4.terbangaja.data.source.network.model.data.AirlinesData
import com.andc4.terbangaja.data.source.network.model.data.AirportsData
import com.andc4.terbangaja.data.source.network.model.data.FlightsData
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface TerbangAjaApiService {
    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): Response<BaseResponse<LoginData>>

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

    @POST("auth/forgot-password")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequest,
    ): Response<ForgotPasswordResponse>

    @GET("auth")
    suspend fun getAuth(): Response<BaseResponse<ProfileData>>

    @GET("flights")
    suspend fun getFlights(
        @Query("page") page: Int? = 1,
        @Query("limit") limit: Int? = 30,
    ): Response<BaseResponse<BasePaging<List<FlightsData>>>>

    @GET("airports")
    suspend fun getAirports(
        @Query("page") page: Int? = 1,
        @Query("limit") limit: Int? = 30,
        @Query("search") search: String? = null,
    ): Response<BaseResponse<List<AirportsData>>>

    @GET("airports/{id}")
    suspend fun getAirportsById(
        @Path("id") id: Int,
    ): Response<BaseResponse<AirportsData>>

    @GET("airlines")
    suspend fun getAirlines(): Response<BaseResponse<List<AirlinesData>>>

    @GET("airlines/{id}")
    suspend fun getAirlinesById(
        @Path("id") id: Int,
    ): Response<BaseResponse<AirlinesData>>

    companion object {
        @JvmStatic
        operator fun invoke(context: Context): TerbangAjaApiService {
            val sharedPreferences =
                context.getSharedPreferences(AuthPreferenceImpl.PREF_NAME, Context.MODE_PRIVATE)
            val authPreference = AuthPreferenceImpl(sharedPreferences)
            val okHttpClient =
                OkHttpClient.Builder()
                    .connectTimeout(300, TimeUnit.SECONDS)
                    .readTimeout(300, TimeUnit.SECONDS)
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
