package com.andc4.terbangaja.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andc4.terbangaja.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers

class HistoryViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    fun isLogin() = authRepository.isLogin().asLiveData(Dispatchers.IO)
}
