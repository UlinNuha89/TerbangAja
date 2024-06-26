package com.andc4.terbangaja.data.repository

import com.andc4.terbangaja.data.datasource.AuthDataSource
import com.andc4.terbangaja.data.mapper.toProfile
import com.andc4.terbangaja.data.mapper.toRegister
import com.andc4.terbangaja.data.model.Register
import com.andc4.terbangaja.data.model.User
import com.andc4.terbangaja.utils.ResultWrapper
import com.andc4.terbangaja.utils.proceedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

interface AuthRepository {
    fun doRegister(
        name: String,
        email: String,
        phoneNumber: String,
        password: String,
    ): Flow<ResultWrapper<Register>>

    fun doVerifyOtp(
        email: String,
        otp: String,
    ): Flow<ResultWrapper<String>>

    fun doResendOtp(): Flow<ResultWrapper<String>>

    @Throws(exceptionClasses = [java.lang.Exception::class])
    fun doLogin(
        email: String,
        password: String,
    ): Flow<ResultWrapper<String>>

    fun doSendEmailForgotPassword(email: String): Flow<ResultWrapper<String?>>

    fun doLogout()

    fun isLogin(): Boolean

    fun renewToken()

    fun getProfile(): Flow<ResultWrapper<User>>

    fun updateProfile(
        id: String,
        name: String,
        email: String,
        phoneNumber: String,
        image: File?,
    ): Flow<ResultWrapper<User>>
}

class AuthRepositoryImpl(private val dataSource: AuthDataSource) : AuthRepository {
    override fun doRegister(
        name: String,
        email: String,
        phoneNumber: String,
        password: String,
    ): Flow<ResultWrapper<Register>> {
        return proceedFlow {
            dataSource.doRegister(
                name = name,
                email = email,
                password = password,
                phoneNumber = phoneNumber,
            ).toRegister()
        }.catch {
            emit(ResultWrapper.Error(Exception(it)))
        }
    }

    override fun doVerifyOtp(
        email: String,
        otp: String,
    ): Flow<ResultWrapper<String>> {
        return proceedFlow {
            dataSource.doVerifyOtp(email, otp).message
        }.catch {
            emit(ResultWrapper.Error(Exception(it)))
        }
    }

    override fun doResendOtp(): Flow<ResultWrapper<String>> {
        return proceedFlow {
            dataSource.doResendOtp().message
        }.catch {
            emit(ResultWrapper.Error(Exception(it)))
        }
    }

    override fun doLogin(
        email: String,
        password: String,
    ): Flow<ResultWrapper<String>> {
        return proceedFlow {
            dataSource.doLogin(email, password).message
        }.catch {
            emit(ResultWrapper.Error(Exception(it)))
        }
    }

    override fun doSendEmailForgotPassword(email: String): Flow<ResultWrapper<String?>> {
        return proceedFlow {
            dataSource.forgotPassword(email).message
        }.catch {
            emit(ResultWrapper.Error(Exception(it)))
        }
    }

    override fun doLogout() {
        return dataSource.deleteAuth()
    }

    override fun isLogin(): Boolean {
        return if (dataSource.getToken() == null) {
            false
        } else {
            renewToken()
            true
        }
    }

    override fun renewToken() {
        doLogin(dataSource.getEmail()!!, dataSource.getPass()!!)
    }

    override fun getProfile(): Flow<ResultWrapper<User>> {
        return proceedFlow {
            dataSource.getProfile().toProfile()
        }.catch {
            emit(ResultWrapper.Error(Exception(it)))
        }
    }

    override fun updateProfile(
        id: String,
        name: String,
        email: String,
        phoneNumber: String,
        image: File?,
    ): Flow<ResultWrapper<User>> {
        return proceedFlow {
            dataSource.updateProfile(
                id,
                name,
                email,
                phoneNumber,
                image?.let {
                    val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("photo", it.name, requestFile)
                },
            ).toProfile()
        }.catch {
            emit(ResultWrapper.Error(Exception(it)))
        }
    }
}
