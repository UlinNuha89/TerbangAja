package com.andc4.terbangaja.presentation.resetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andc4.terbangaja.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers

class ResetPasswordViewModel(private val repository: AuthRepository) : ViewModel() {
    fun sendEmail(email: String) = repository.doSendEmailForgotPassword(email).asLiveData(Dispatchers.IO)
}
