package com.andc4.terbangaja.presentation.history

import androidx.lifecycle.ViewModel
import com.andc4.terbangaja.data.repository.AuthRepository

class HistoryViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    fun isLogin() = authRepository.isLogin()
}
