package com.andc4.terbangaja.data.datasource

import com.andc4.terbangaja.data.source.local.pref.AuthPreference
import com.andc4.terbangaja.data.source.network.model.BaseResponse
import com.andc4.terbangaja.data.source.network.model.auth.forgotpassword.ForgotPasswordRequest
import com.andc4.terbangaja.data.source.network.model.auth.forgotpassword.ForgotPasswordResponse
import com.andc4.terbangaja.data.source.network.model.auth.login.LoginData
import com.andc4.terbangaja.data.source.network.model.auth.login.LoginRequest
import com.andc4.terbangaja.data.source.network.model.auth.otp.OtpData
import com.andc4.terbangaja.data.source.network.model.auth.otp.OtpRequestPayload
import com.andc4.terbangaja.data.source.network.model.auth.profile.ProfileData
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

    suspend fun getProfile(): BaseResponse<ProfileData>

    @Throws(exceptionClasses = [Exception::class])
    suspend fun forgotPassword(email: String): ForgotPasswordResponse

    fun setTokenOtp(token: String)

    fun getTokenOtp(): String?

    fun getToken(): String?

    fun getEmail(): String?

    fun getPass(): String?

    fun deleteTokenOtp()

    fun deleteAuth()

    fun setAuth(
        token: String,
        email: String,
        password: String,
    )
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
                setAuth(responseData?.token!!, email, password)
                return response.body() ?: throw Exception("Empty response body")
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, BaseResponse::class.java)
                throw Exception(errorResponse.message)
            }
        } catch (e: Exception) {
            // Handle the exception, log it, or rethrow it as needed
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

        // return service.register(nameBody, emailBody, passwordBody, image, phoneNumberBody)
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
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, BaseResponse::class.java)
                throw Exception(errorResponse.message)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getProfile(): BaseResponse<ProfileData> {
        return try {
            val response = service.getAuth()
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

    override fun getEmail(): String? = pref.getEmail()

    override fun getPass(): String? = pref.getPass()

    override fun deleteTokenOtp() = pref.deleteTokenOtp()

    override fun deleteAuth() = pref.deleteAuth()

    override fun setAuth(
        token: String,
        email: String,
        password: String,
    ) {
        pref.setToken(token)
        pref.setEmail(email)
        pref.setPass(password)
    }
}
