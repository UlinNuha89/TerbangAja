package com.andc4.terbangaja.presentation.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andc4.terbangaja.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers

class AccountViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    fun isLogin() = authRepository.isLogin()

    fun getProfile() = authRepository.getProfile().asLiveData(Dispatchers.IO)

    fun doLogout() = authRepository.doLogout()
}
