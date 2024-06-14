package com.andc4.terbangaja.presentation.notification

import androidx.lifecycle.ViewModel
import com.andc4.terbangaja.data.repository.AuthRepository

class NotificationViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    fun isLogin() = authRepository.isLogin()
}
