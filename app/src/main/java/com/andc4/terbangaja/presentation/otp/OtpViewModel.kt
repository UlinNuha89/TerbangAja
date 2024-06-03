package com.andc4.terbangaja.presentation.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andc4.terbangaja.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers

class OtpViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    fun doVerify(
        email: String,
        otp: String,
    ) = authRepository.doVerifyOtp(email, otp).asLiveData(Dispatchers.IO)

    fun doResend() = authRepository.doResendOtp().asLiveData(Dispatchers.IO)
}
