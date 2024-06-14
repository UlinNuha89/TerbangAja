package com.andc4.terbangaja.data.repository

import com.andc4.terbangaja.data.datasource.AuthDataSource
import com.andc4.terbangaja.data.mapper.toRegister
import com.andc4.terbangaja.data.model.Register
import com.andc4.terbangaja.utils.ResultWrapper
import com.andc4.terbangaja.utils.proceedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

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

    fun verifyToken(): Pair<String, Boolean>

    fun doSendEmailForgotPassword(email: String): Flow<ResultWrapper<String?>>

    fun doLogout()

    fun isLogin(): Boolean

    fun renewToken()

    fun getProfile(): Flow<ResultWrapper<String>>
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

    override fun verifyToken(): Pair<String, Boolean> {
        val token = dataSource.getTokenOtp()
        return if (token != null) {
            Pair(token, true)
        } else {
            Pair("Token is empty", false)
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
        return dataSource.getTokenOtp() != null
    }

    override fun renewToken() {
        doLogin(dataSource.getEmail()!!, dataSource.getPass()!!)
    }

    override fun getProfile(): Flow<ResultWrapper<String>> {
        TODO("Not yet implemented")
    }
}
