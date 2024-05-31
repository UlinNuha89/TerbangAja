package com.andc4.terbangaja.data.repository

import com.andc4.terbangaja.data.datasource.AuthDataSource
import com.andc4.terbangaja.utils.ResultWrapper
import com.andc4.terbangaja.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    @Throws(exceptionClasses = [java.lang.Exception::class])
    fun doRegister(
        name: String,
        email: String,
        phoneNumber: String,
        password: String,
    ): Flow<ResultWrapper<Boolean>>
}

class AuthRepositoryImpl(private val dataSource: AuthDataSource) : AuthRepository {
    override fun doRegister(
        name: String,
        email: String,
        phoneNumber: String,
        password: String,
    ): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.doRegister(name = name, email = email, password = password, phoneNumber = phoneNumber) }
    }
}
