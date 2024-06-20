package com.andc4.terbangaja.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andc4.terbangaja.data.repository.AuthRepository
import com.andc4.terbangaja.data.repository.NotificationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers

class NotificationViewModel(
    private val authRepository: AuthRepository,
    private val notificationRepository: NotificationRepository,
) : ViewModel() {
    fun getNotifications() = notificationRepository.getNotification().asLiveData(Dispatchers.IO)

    fun isLogin() = authRepository.isLogin()
    fun isLogin() = authRepository.isLogin().asLiveData(Dispatchers.IO)
}
