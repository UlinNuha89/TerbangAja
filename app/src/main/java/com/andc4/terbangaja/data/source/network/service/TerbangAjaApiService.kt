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
import com.andc4.terbangaja.data.source.network.model.auth.profile.Profile
import com.andc4.terbangaja.data.source.network.model.auth.register.RegisterData
import com.andc4.terbangaja.data.source.network.model.data.AirlinesData
import com.andc4.terbangaja.data.source.network.model.data.AirportsData
import com.andc4.terbangaja.data.source.network.model.data.FlightsData
import com.andc4.terbangaja.data.source.network.model.data.FlightsTicket
import com.andc4.terbangaja.data.source.network.model.data.SeatData
import com.andc4.terbangaja.data.source.network.model.history.BookingData
import com.andc4.terbangaja.data.source.network.model.notification.Notification
import com.andc4.terbangaja.data.source.network.model.data.dobooking.BookingRequestPayload
import com.andc4.terbangaja.data.source.network.model.data.dobooking.BookingResponse
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
import retrofit2.http.PUT
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
    suspend fun getAuth(): Response<BaseResponse<Profile>>

    @Multipart
    @PUT("auth")
    suspend fun updateProfile(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part photo: MultipartBody.Part? = null,
    ): Response<BaseResponse<Profile>>

    @GET("flights")
    suspend fun getFlights(
        @Query("page") page: Int? = 1,
        @Query("limit") limit: Int? = 30,
    ): Response<BaseResponse<BasePaging<List<FlightsData>>>>

    @GET("seats/filter")
    suspend fun getSeat(
        @Query("flightId") flightId: Int? = 1,
        @Query("seatClass") seatClass: String? = "30",
    ): Response<BaseResponse<List<SeatData>>>

    @Multipart
    @POST("findTickets")
    suspend fun getFlightsTicket(
        @Query("page") page: Int? = 1,
        @Query("limit") limit: Int? = 30,
        @Part("from") from: RequestBody,
        @Part("to") to: RequestBody,
        @Part("departure_date") departureDate: RequestBody,
        @Part("total_passengers") totalPassengers: RequestBody,
        @Part("seat_class") seatClass: RequestBody,
        @Part("filter") filter: RequestBody? = null,
        @Part("return_date") returnDate: RequestBody? = null,
    ): Response<BaseResponse<FlightsTicket<BasePaging<List<FlightsData>>>>>

    @GET("airports")
    suspend fun getAirports(
        @Query("page") page: Int? = 1,
        @Query("limit") limit: Int? = 30,
        @Query("search") search: String? = null,
    ): Response<BaseResponse<BasePaging<List<AirportsData>>>>

    @POST("bookings")
    suspend fun doBooking(
        @Body payload: BookingRequestPayload,
    ): Response<BaseResponse<BookingResponse>>

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

    @GET("notifications")
    suspend fun getNotifications(): Response<BaseResponse<List<Notification>>>

    @PUT("notifications/{id}")
    suspend fun updateNotification(
        @Path("id") id: Int,
    ): Response<BaseResponse<Notification>>

    @Multipart
    @POST("bookingHistories")
    suspend fun getBookingHistories(
        @Query("page") page: Int? = 1,
        @Query("limit") limit: Int? = 15,
        @Part("startDate") startDate: RequestBody? = null,
        @Part("endDate") endDate: RequestBody? = null,
        @Part("code") code: RequestBody? = null,
    ): Response<BaseResponse<BookingData>>

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
