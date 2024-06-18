package com.andc4.terbangaja.presentation.account

import androidx.lifecycle.ViewModel
import com.andc4.terbangaja.data.repository.AuthRepository

class AccountViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    fun isLogin() = authRepository.isLogin()

    fun doLogout() = authRepository.doLogout()
}
