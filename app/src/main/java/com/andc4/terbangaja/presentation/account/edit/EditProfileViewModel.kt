package com.andc4.terbangaja.presentation.account.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andc4.terbangaja.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import java.io.File

class EditProfileViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    fun getProfile() = authRepository.getProfile().asLiveData(Dispatchers.IO)

    fun updateProfile(
        name: String,
        email: String,
        phoneNumber: String,
        image: File?,
    ) = authRepository.updateProfile(name, email, phoneNumber, image).asLiveData(Dispatchers.IO)
}
