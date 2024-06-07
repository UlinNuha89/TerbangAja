package com.andc4.terbangaja.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andc4.terbangaja.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers

class LoginViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    fun doLogin(
        email: String,
        password: String,
    ) = authRepository.doLogin(
        email = email,
        password = password,
    ).asLiveData(Dispatchers.IO)

    fun getToken() = authRepository.verifyToken().first
}
