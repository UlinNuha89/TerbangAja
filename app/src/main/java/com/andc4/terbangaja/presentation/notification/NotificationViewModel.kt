package com.andc4.terbangaja.presentation.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andc4.terbangaja.data.model.Notification
import com.andc4.terbangaja.data.repository.AuthRepository
import com.andc4.terbangaja.data.repository.NotificationRepository
import com.andc4.terbangaja.utils.ResultWrapper
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers

class NotificationViewModel(
    private val authRepository: AuthRepository,
    private val notificationRepository: NotificationRepository,
) : ViewModel() {
//    fun getNotifications() = notificationRepository.getNotification().asLiveData(Dispatchers.IO)
//
//    fun updateNotification(id: Int) = notificationRepository.updateNotification(id).asLiveData(Dispatchers.IO)
//
//    fun isLogin() = authRepository.isLogin()
    private val _notifications = MutableLiveData<ResultWrapper<List<Notification>>>()
    val notifications: LiveData<ResultWrapper<List<Notification>>> get() = _notifications

    private val _updateResult = MutableLiveData<ResultWrapper<String>>()
    val updateResult: LiveData<ResultWrapper<String>> get() = _updateResult

    fun getNotifications() {
        viewModelScope.launch {
            notificationRepository.getNotification().collect { result ->
                _notifications.value = result
            }
        }
    }

    fun updateNotification(id: Int) {
        viewModelScope.launch {
            notificationRepository.updateNotification(id).collect { result ->
                _updateResult.value = result
                getNotifications() // Refresh notifications after update
            }
        }
    }

    fun isLogin(): Boolean {
        return authRepository.isLogin()
    }
    fun isLogin() = authRepository.isLogin()
    fun isLogin() = authRepository.isLogin().asLiveData(Dispatchers.IO)
}
