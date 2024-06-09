package com.andc4.terbangaja.data.datasource

import com.andc4.terbangaja.data.source.local.pref.AuthPreference
import com.andc4.terbangaja.data.source.network.model.BaseResponse
import com.andc4.terbangaja.data.source.network.model.auth.forgotpassword.ForgotPasswordRequest
import com.andc4.terbangaja.data.source.network.model.auth.forgotpassword.ForgotPasswordResponse
import com.andc4.terbangaja.data.source.network.model.auth.login.LoginData
import com.andc4.terbangaja.data.source.network.model.auth.login.LoginRequest
import com.andc4.terbangaja.data.source.network.model.auth.otp.OtpData
import com.andc4.terbangaja.data.source.network.model.auth.otp.OtpRequestPayload
import com.andc4.terbangaja.data.source.network.model.auth.register.RegisterData
import com.andc4.terbangaja.data.source.network.service.TerbangAjaApiService
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

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

    @Throws(exceptionClasses = [Exception::class])
    suspend fun forgotPassword(email: String): ForgotPasswordResponse

    fun setToken(token: String)

    fun getToken(): String?

    fun deleteToken()
}

class AuthDataSourceImpl(
    private val service: TerbangAjaApiService,
    private val pref: AuthPreference,
) : AuthDataSource {
    var tokenOtp: String? = null

    override suspend fun doLogin(
        email: String,
        password: String,
    ): BaseResponse<LoginData> {
        return service.login(LoginRequest(email, password))
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

        // return service.register(nameBody, emailBody, passwordBody, image, phoneNumberBody)
        return try {
            val response =
                service.register(nameBody, emailBody, passwordBody, image, phoneNumberBody)
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Empty response body")
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, BaseResponse::class.java)
                BaseResponse(errorResponse.message, null)
            }
        } catch (e: Exception) {
            // Handle the exception, log it, or rethrow it as needed
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
                BaseResponse("Verifikasi OTP gagal", null)
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
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, BaseResponse::class.java)
                BaseResponse(errorResponse.message, null)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun forgotPassword(email: String): ForgotPasswordResponse {
        return service.forgotPassword(ForgotPasswordRequest(email))
    }

    override fun setToken(token: String) {
        return pref.setToken(token)
    }

    override fun getToken(): String? = pref.getToken()

    override fun deleteToken() = pref.deleteToken()
}
