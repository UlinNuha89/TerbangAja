package com.andc4.terbangaja.presentation.account.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andc4.terbangaja.data.repository.AuthRepository
import com.andc4.terbangaja.data.source.local.pref.AuthPreference
import kotlinx.coroutines.Dispatchers
import java.io.File

class EditProfileViewModel(
    private val authRepository: AuthRepository,
    private val pref: AuthPreference,
) : ViewModel() {
    fun getProfile() = authRepository.getProfile().asLiveData(Dispatchers.IO)

    fun updateProfile(
        id: String,
        name: String,
        email: String,
        phoneNumber: String,
        image: File?,
    ) = authRepository.updateProfile(id, name, email, phoneNumber, image).asLiveData(Dispatchers.IO)

    fun getUserID() = pref.getUserID()
}
