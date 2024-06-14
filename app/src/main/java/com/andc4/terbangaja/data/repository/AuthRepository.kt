package com.andc4.terbangaja.data.repository

import com.andc4.terbangaja.data.datasource.AuthDataSource
import com.andc4.terbangaja.data.mapper.toRegister
import com.andc4.terbangaja.data.model.Register
import com.andc4.terbangaja.utils.ResultWrapper
import com.andc4.terbangaja.utils.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart

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

    fun doLogin(
        email: String,
        password: String,
    ): Flow<ResultWrapper<String>>

    fun verifyToken(): Pair<String, Boolean>

    fun doSendEmailForgotPassword(email: String): Flow<ResultWrapper<String?>>

    fun doLogout()
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
            val response = dataSource.doLogin(email, password)
            if (response.data != null) {
                response.data.let { dataSource.setToken(it.token) }
                response.message
            } else {
                if (response.message.startsWith("User") || response.message.startsWith("Invalid")) {
                    throw Exception("Email atau Password Salah!")
                } else {
                    throw Exception("Unknown Error")
                }
            }
        }.catch { e ->
            val errorMessage = e.message ?: "Unknown exception"
            emit(ResultWrapper.Error(Exception(errorMessage)))
        }.onStart {
            emit(ResultWrapper.Loading())
            delay(2000)
        }
    }

    override fun verifyToken(): Pair<String, Boolean> {
        val token = dataSource.getToken()
        return if (token != null) {
            Pair(token, true)
        } else {
            Pair("Token is empty", false)
        }
    }

    override fun doSendEmailForgotPassword(email: String): Flow<ResultWrapper<String?>> {
        return proceedFlow {
            val response = dataSource.forgotPassword(email)
            response.message
        }.catch {
            emit(ResultWrapper.Error(Exception(it)))
        }
    }

    override fun doLogout() {
        return dataSource.deleteToken()
    }
}
