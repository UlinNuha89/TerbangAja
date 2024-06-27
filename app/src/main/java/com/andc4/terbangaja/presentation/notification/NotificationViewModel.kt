package com.andc4.terbangaja.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andc4.terbangaja.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers

class NotificationViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    fun isLogin() = authRepository.isLogin().asLiveData(Dispatchers.IO)
}
