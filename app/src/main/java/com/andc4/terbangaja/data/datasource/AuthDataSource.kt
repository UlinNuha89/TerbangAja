package com.andc4.terbangaja.data.datasource

import com.andc4.terbangaja.data.source.local.pref.AuthPreference
import com.andc4.terbangaja.data.source.network.model.BaseResponse
import com.andc4.terbangaja.data.source.network.model.auth.forgotpassword.ForgotPasswordRequest
import com.andc4.terbangaja.data.source.network.model.auth.forgotpassword.ForgotPasswordResponse
import com.andc4.terbangaja.data.source.network.model.auth.login.LoginData
import com.andc4.terbangaja.data.source.network.model.auth.login.LoginRequest
import com.andc4.terbangaja.data.source.network.model.auth.otp.OtpData
import com.andc4.terbangaja.data.source.network.model.auth.otp.OtpRequestPayload
import com.andc4.terbangaja.data.source.network.model.auth.profile.Profile
import com.andc4.terbangaja.data.source.network.model.auth.register.RegisterData
import com.andc4.terbangaja.data.source.network.service.TerbangAjaApiService
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.net.HttpURLConnection

interface AuthDataSource {
    @Throws(exceptionClasses = [Exception::class])
    suspend fun doLogin(
        email: String,
        password: String,
    ): BaseResponse<LoginData>

    @Throws(exceptionClasses = [Exception::class])
    suspend fun doRegister(
        name: String,
        email: String,
        password: String,
        image: MultipartBody.Part? = null,
        phoneNumber: String,
    ): BaseResponse<RegisterData>

    @Throws(exceptionClasses = [Exception::class])
    suspend fun doVerifyOtp(
        email: String,
        otp: String,
    ): BaseResponse<OtpData>

    suspend fun doResendOtp(): BaseResponse<OtpData>

    suspend fun getProfile(): BaseResponse<Profile>

    suspend fun updateProfile(
        name: String,
        email: String,
        phoneNumber: String,
        image: MultipartBody.Part?,
    ): BaseResponse<Profile>

    @Throws(exceptionClasses = [Exception::class])
    suspend fun forgotPassword(email: String): ForgotPasswordResponse

    fun setTokenOtp(token: String)

    fun getTokenOtp(): String?

    fun getToken(): String?

    fun deleteTokenOtp()

    fun deleteToken()

    fun setToken(token: String)
}

class AuthDataSourceImpl(
    private val service: TerbangAjaApiService,
    private val pref: AuthPreference,
) : AuthDataSource {
    override suspend fun doLogin(
        email: String,
        password: String,
    ): BaseResponse<LoginData> {
        try {
            val response = service.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                val responseData = response.body()?.data
                setToken(responseData?.token!!)
                return response.body() ?: throw Exception("Empty response body")
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, BaseResponse::class.java)
                throw Exception(errorResponse.message)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun doRegister(
        name: String,
        email: String,
        password: String,
        image: MultipartBody.Part?,
        phoneNumber: String,
    ): BaseResponse<RegisterData> {
        val nameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), name)
        val emailBody = RequestBody.create("text/plain".toMediaTypeOrNull(), email)
        val passwordBody = RequestBody.create("text/plain".toMediaTypeOrNull(), password)
        val phoneNumberBody = RequestBody.create("text/plain".toMediaTypeOrNull(), phoneNumber)

        try {
            val response =
                service.register(nameBody, emailBody, passwordBody, image, phoneNumberBody)
            if (response.isSuccessful) {
                setTokenOtp(response.body()?.data?.token!!)
                return response.body() ?: throw Exception("Empty response body")
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, BaseResponse::class.java)
                throw Exception(errorResponse.message)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun doVerifyOtp(
        email: String,
        otp: String,
    ): BaseResponse<OtpData> {
        return try {
            val response = service.verifyOTP(OtpRequestPayload(email, otp))
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Empty Response body")
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, BaseResponse::class.java)
                throw Exception(errorResponse.message)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun doResendOtp(): BaseResponse<OtpData> {
        return try {
            val response = service.resendOTP()
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Empty Response body")
            } else {
                when (response.code()) {
                    HttpURLConnection.HTTP_UNAUTHORIZED -> throw Exception(response.code().toString())
                    HttpURLConnection.HTTP_INTERNAL_ERROR -> throw Exception(response.code().toString())
                    else -> {
                        val errorBody = response.errorBody()?.string()
                        val errorResponse = Gson().fromJson(errorBody, BaseResponse::class.java)
                        throw Exception(errorResponse.message)
                    }
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getProfile(): BaseResponse<Profile> {
        return try {
            val response = service.getAuth()
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Empty Response body")
            } else {
                when (response.code()) {
                    HttpURLConnection.HTTP_UNAUTHORIZED -> throw Exception(response.code().toString())
                    HttpURLConnection.HTTP_INTERNAL_ERROR -> throw Exception(response.code().toString())
                    else -> {
                        val errorBody = response.errorBody()?.string()
                        val errorResponse = Gson().fromJson(errorBody, BaseResponse::class.java)
                        throw Exception(errorResponse.message)
                    }
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updateProfile(
        name: String,
        email: String,
        phoneNumber: String,
        image: MultipartBody.Part?,
    ): BaseResponse<Profile> {
        try {
            val nameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), name)
            val emailBody = RequestBody.create("text/plain".toMediaTypeOrNull(), email)
            val phoneBody = RequestBody.create("text/plain".toMediaTypeOrNull(), phoneNumber)

            val response = service.updateProfile(nameBody, emailBody, phoneBody, image)
            if (response.isSuccessful) {
                return response.body() ?: throw Exception("Empty response body")
            } else {
                when (response.code()) {
                    HttpURLConnection.HTTP_UNAUTHORIZED -> throw Exception(response.code().toString())
                    HttpURLConnection.HTTP_INTERNAL_ERROR -> throw Exception(response.code().toString())
                    else -> {
                        val errorBody = response.errorBody()?.string()
                        val errorResponse = Gson().fromJson(errorBody, BaseResponse::class.java)
                        throw Exception(errorResponse.message)
                    }
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun forgotPassword(email: String): ForgotPasswordResponse {
        return try {
            val response = service.forgotPassword(ForgotPasswordRequest(email))
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Empty Response body")
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, BaseResponse::class.java)
                throw Exception(errorResponse.message)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override fun setTokenOtp(token: String) {
        return pref.setTokenOtp(token)
    }

    override fun getTokenOtp(): String? = pref.getTokenOtp()

    override fun getToken(): String? = pref.getToken()

    override fun deleteTokenOtp() = pref.deleteTokenOtp()

    override fun deleteToken() = pref.deleteToken()

    override fun setToken(token: String) = pref.setToken(token)
}
